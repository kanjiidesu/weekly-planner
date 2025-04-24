package com.weeklyPlanner.controller;

import com.weeklyPlanner.model.CreatePurchaseListRequest;
import com.weeklyPlanner.model.PurchaseItem;
import com.weeklyPlanner.model.PurchaseList;
import com.weeklyPlanner.model.User;
import com.weeklyPlanner.repository.PurchaseItemRepository;
import com.weeklyPlanner.repository.PurchaseListRepository;
import com.weeklyPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/purchase-list")
@CrossOrigin(origins = "http://localhost:4200")
public class PurchaseListController {

    @Autowired
    private PurchaseListRepository purchaseListRepository;

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Autowired
    private UserRepository userRepository;

    // Endpoint to create a purchase list and associate it with a user
    @PostMapping
    @Transactional
    public PurchaseList createPurchaseList(@RequestBody CreatePurchaseListRequest request) {
        // Find the user associated with the purchase list
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Try to find an existing purchase list with the same name for this user
        Optional<PurchaseList> existingListOpt = purchaseListRepository
                .findByPurchaseListNameAndUsersContaining(request.getPurchaseListName(), user);

        PurchaseList purchaseList;

        if (existingListOpt.isPresent()) {
            // If the list already exists, use it
            purchaseList = existingListOpt.get();
        } else {
            // Otherwise, create a new purchase list
            purchaseList = new PurchaseList();
            purchaseList.setPurchaseListName(request.getPurchaseListName());
            purchaseList.setUsers(Collections.singletonList(user));
        }

        // Create a new PurchaseItem and add it to the PurchaseList
        PurchaseItem item = new PurchaseItem(request.getItemName(), request.getQuantity());

        // IMPORTANT: Use the addItem() method to link the PurchaseItem to the PurchaseList
        purchaseList.addItem(item);  // Adds the item and sets the relationship

        // Save the PurchaseItem and PurchaseList (CascadeType.ALL handles it)
        purchaseItemRepository.save(item);   // Saving the PurchaseItem explicitly (though cascade should handle it)
        PurchaseList savedList = purchaseListRepository.save(purchaseList);  // Saving the PurchaseList

        return savedList;  // Return the saved PurchaseList
    }

    @PostMapping("/create")
    public ResponseEntity<PurchaseList> createEmptyList(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        String listName = request.get("purchaseListName").toString();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<PurchaseList> existing = purchaseListRepository.findByPurchaseListNameAndUsersContaining(listName, user);
        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }

        PurchaseList newList = new PurchaseList();
        newList.setPurchaseListName(listName);
        newList.setUsers(Collections.singletonList(user));
        PurchaseList saved = purchaseListRepository.save(newList);

        return ResponseEntity.ok(saved);
    }

    // Endpoint to remove an item from a purchase list
    @DeleteMapping("/item/{id}")
    @Transactional
    public ResponseEntity<String> removeItem(@PathVariable Long id) {
        try {
            // Try to find the PurchaseItem by ID
            PurchaseItem item = purchaseItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Purchase item not found"));

            PurchaseList list = item.getPurchaseList();

            // Log item and associated list
            System.out.println("Attempting to remove item: " + item.getItemName());
            if (list != null) {
                System.out.println("Associated PurchaseList: " + list.getPurchaseListName());
                list.getItems().remove(item); // Remove item from list (in-memory)
            }

            // Delete the item from the database
            purchaseItemRepository.delete(item);

            // If the list has no more items, delete the list
            if (list != null && list.getItems().isEmpty()) {
                // Detach from all users before deletion (break many-to-many relation)
                list.getUsers().clear();
                purchaseListRepository.delete(list);
                System.out.println("PurchaseList '" + list.getPurchaseListName() + "' deleted as it became empty.");
                return ResponseEntity.ok("Item removed and empty list deleted successfully");
            }

            return ResponseEntity.ok("Item removed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing item: " + e.getMessage());
        }
    }

    @DeleteMapping("/{listId}")
    @Transactional
    public ResponseEntity<String> deleteList(@PathVariable Long listId) {
        Optional<PurchaseList> optionalList = purchaseListRepository.findById(listId);
        if (optionalList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List not found");
        }

        PurchaseList list = optionalList.get();

        // Remove from all users (if many-to-many)
        list.getUsers().clear();

        // Delete all items first
        purchaseItemRepository.deleteAll(list.getItems());

        // Delete the list
        list.getUsers().clear();
        purchaseListRepository.delete(list);

        return ResponseEntity.ok("List deleted successfully");
    }


    // Get all purchase lists of a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PurchaseList>> getUserPurchaseLists(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(purchaseListRepository.findAllByUsersContaining(user));
    }

    // Share the list with another user
    @PostMapping("/{listId}/share")
    public ResponseEntity<String> shareListWithUser(
            @PathVariable Long listId,
            @RequestParam String username
    ) {
        PurchaseList list = purchaseListRepository.findById(listId)
                .orElseThrow(() -> new RuntimeException("Purchase list not found"));

        User userToShareWith = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!list.getUsers().contains(userToShareWith)) {
            list.getUsers().add(userToShareWith);
            purchaseListRepository.save(list);
        }

        return ResponseEntity.ok("List shared with " + username);
    }

    // Get a list by its name
    @GetMapping("/by-list/{listName}")
    public List<PurchaseList> getItemsByListName(@PathVariable String purchaseListName) {
        return purchaseListRepository.findByPurchaseListName(purchaseListName);
    }

    // Get all purchase lists grouped by their name
    @GetMapping("/grouped")
    public Map<String, List<PurchaseList>> getGroupedLists() {
        List<PurchaseList> all = purchaseListRepository.findAll();

        return all.stream()
                .collect(Collectors.groupingBy(PurchaseList::getPurchaseListName));
    }
}
