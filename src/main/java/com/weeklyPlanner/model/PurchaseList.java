package com.weeklyPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "purchaseList")
public class PurchaseList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long purchaseListId;

    @Column(name = "itemName", nullable = false)
    private String itemName;

    @Column(name = "quantity", nullable = true)
    private String quantity;

    public PurchaseList() {

    }


    public long getPurchaseListId() {
        return purchaseListId;
    }

    public void setPurchaseListId(long purchaseListId) {
        this.purchaseListId = purchaseListId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setUser(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() { return quantity; }

    public void setQuantity() { this.quantity = quantity; }

}
