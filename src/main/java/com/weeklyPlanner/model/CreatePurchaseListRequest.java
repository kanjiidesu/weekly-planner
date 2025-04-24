package com.weeklyPlanner.model;

public class CreatePurchaseListRequest {

    private Long userId;
    private String purchaseListName;
    private String itemName;
    private Long quantity;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPurchaseListName() {
        return purchaseListName;
    }

    public void setPurchaseListName(String purchaseListName) {
        this.purchaseListName = purchaseListName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
