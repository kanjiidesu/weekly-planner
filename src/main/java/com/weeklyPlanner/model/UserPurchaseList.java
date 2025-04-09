package com.weeklyPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "userPurchaseList")
public class UserPurchaseList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userPurchaseListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseListId", nullable = false)
    private PurchaseList purchaseList;

    public UserPurchaseList() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PurchaseList getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(PurchaseList purchaseList) {
        this.purchaseList = purchaseList;
    }



}
