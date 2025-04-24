package com.weeklyPlanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "purchaseItem")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_list_id")
    private PurchaseList purchaseList;

    @Column(name = "itemName", nullable = false)
    private String itemName;

    @Column(name = "quantity", nullable = false)
    private long quantity;

    public PurchaseItem() {
    }

    public PurchaseItem(String itemName, long quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public PurchaseList getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(PurchaseList purchaseList) {
        this.purchaseList = purchaseList;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
