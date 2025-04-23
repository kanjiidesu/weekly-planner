package com.weeklyPlanner.controller;

import com.weeklyPlanner.model.PurchaseList;
import com.weeklyPlanner.model.User;
import com.weeklyPlanner.repository.PurchaseListRepository;
import com.weeklyPlanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/purchase-list")
@CrossOrigin(origins = "http://localhost:4200")
public class PurchaseListController {

    @Autowired
    private PurchaseListRepository purchaseListRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public PurchaseList addItem(@RequestBody PurchaseList item) {
        return purchaseListRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id) {
        purchaseListRepository.deleteById(id);
    }

    @GetMapping
    public List<PurchaseList> getAllItems(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return purchaseListRepository.findAllByUsersContaining(user);
    }
    @GetMapping("/by-list/{listName}")
    public List<PurchaseList> getItemsByListName(@PathVariable String purchaseListName) {
        return purchaseListRepository.findByPurchaseListName(purchaseListName);
    }

    @GetMapping("/grouped")
    public Map<String, List<PurchaseList>> getGroupedLists() {
        List<PurchaseList> all = purchaseListRepository.findAll();

        return all.stream()
                .collect(Collectors.groupingBy(PurchaseList::getPurchaseListName));
    }
}
