package com.weekly_planner.unittests;

import com.weeklyPlanner.controller.PurchaseListController;
import com.weeklyPlanner.model.CreatePurchaseListRequest;
import com.weeklyPlanner.model.PurchaseItem;
import com.weeklyPlanner.model.PurchaseList;
import com.weeklyPlanner.model.User;
import com.weeklyPlanner.repository.PurchaseItemRepository;
import com.weeklyPlanner.repository.PurchaseListRepository;
import com.weeklyPlanner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PurchaseListControllerUnitTest {

    @InjectMocks
    private PurchaseListController purchaseListController;

    @Mock
    private PurchaseListRepository purchaseListRepository;

    @Mock
    private PurchaseItemRepository purchaseItemRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private CreatePurchaseListRequest createPurchaseListRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting up a mocked user with a purchase list with one item
        user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");

        createPurchaseListRequest = new CreatePurchaseListRequest();
        createPurchaseListRequest.setUserId(1L);
        createPurchaseListRequest.setPurchaseListName("Test List");
        createPurchaseListRequest.setItemName("Test Item");
        createPurchaseListRequest.setQuantity(2L);
    }

    @Test
    void testCreatePurchaseList_Success() {
        // Mock the user repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Mock the purchase list repository
        PurchaseList newList = new PurchaseList("Test List");
        when(purchaseListRepository.save(any(PurchaseList.class))).thenReturn(newList);

        // Mock purchase item saving
        PurchaseItem purchaseItem = new PurchaseItem("Test Item", 2);
        when(purchaseItemRepository.save(any(PurchaseItem.class))).thenReturn(purchaseItem);

        // Call the createPurchaseList method
        PurchaseList result = purchaseListController.createPurchaseList(createPurchaseListRequest);

        // Assertions
        assertNotNull(result);
        assertEquals("Test List", result.getPurchaseListName());
        verify(purchaseListRepository, times(1)).save(any(PurchaseList.class));
        verify(purchaseItemRepository, times(1)).save(any(PurchaseItem.class));
    }

    @Test
    void testCreatePurchaseList_UserNotFound() {
        // Mock the user repository to return empty
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the createPurchaseList method
        assertThrows(RuntimeException.class, () -> purchaseListController.createPurchaseList(createPurchaseListRequest));
    }

    @Test
    void testRemoveItem_Success() {
        // Setup
        PurchaseItem item = new PurchaseItem("Test Item", 2);
        PurchaseList list = new PurchaseList("Test List");
        list.addItem(item);
        item.setPurchaseList(list);

        when(purchaseItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(purchaseListRepository.findById(1L)).thenReturn(Optional.of(list));

        // Call removeItem method
        ResponseEntity<String> response = purchaseListController.removeItem(1L);

        // Assertions
        assertEquals("Item removed and empty list deleted successfully", response.getBody());
        verify(purchaseItemRepository, times(1)).delete(item);
        verify(purchaseListRepository, times(1)).delete(list);
    }

    @Test
    void testDeleteList_Success() {
        // Setup
        PurchaseList list = new PurchaseList("Test List");
        when(purchaseListRepository.findById(1L)).thenReturn(Optional.of(list));

        // Call deleteList method
        ResponseEntity<String> response = purchaseListController.deleteList(1L);

        // Assertions
        assertEquals("List deleted successfully", response.getBody());
        verify(purchaseListRepository, times(1)).delete(list);
    }

    @Test
    void testGetUserPurchaseLists_Success() {
        // Setup
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(purchaseListRepository.findAllByUsersContaining(user)).thenReturn(Collections.singletonList(new PurchaseList("Test List")));

        // Call getUserPurchaseLists method
        ResponseEntity<List<PurchaseList>> response = purchaseListController.getUserPurchaseLists(1L);

        // Assertions
        assertEquals(1, response.getBody().size());
        assertEquals("Test List", response.getBody().get(0).getPurchaseListName());
    }

    @Test
    void testGetUserPurchaseLists_UserNotFound() {
        // Setup
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call getUserPurchaseLists method
        assertThrows(RuntimeException.class, () -> purchaseListController.getUserPurchaseLists(1L));
    }
}
