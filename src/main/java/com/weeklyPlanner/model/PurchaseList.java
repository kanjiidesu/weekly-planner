package com.weeklyPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "purchaseList")
public class PurchaseList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long purchaseListId;
    @Column(name = "purchaseListName", nullable = false)
    private String purchaseListName;

    @Column(name = "itemName", nullable = false)
    private String itemName;

    @Column(name = "quantity", nullable = true)
    private long quantity;

    public PurchaseList() {

    }


    public long getPurchaseListId() {
        return purchaseListId;
    }

    public void setPurchaseListId(long purchaseListId) {
        this.purchaseListId = purchaseListId;
    }

    public String getPurchaseListName() { return purchaseListName; }

    public void setPurchaseListName(String purchaseListName) { this.purchaseListName = purchaseListName; }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) { this.itemName = itemName; }

    public long getQuantity() { return quantity; }

    public void setQuantity(long quantity) { this.quantity = quantity; }

}
