package com.weeklyPlanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchaseLists")
public class PurchaseList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseListId;

    @Column(nullable = false)
    private String purchaseListName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "purchase_list_users",
            joinColumns = @JoinColumn(name = "purchase_list_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonManagedReference
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "purchaseList", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PurchaseItem> items = new ArrayList<>();

    // Constructors
    public PurchaseList() {}

    public PurchaseList(String name) {
        this.purchaseListName = name;
    }

    // Getters and Setters
    public Long getPurchaseListId() {
        return purchaseListId;
    }

    public void setPurchaseListId(Long purchaseListId) {
        this.purchaseListId = purchaseListId;
    }

    public String getPurchaseListName() {
        return purchaseListName;
    }

    public void setPurchaseListName(String purchaseListName) {
        this.purchaseListName = purchaseListName;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }

    public void addItem(PurchaseItem item) {
        items.add(item);
        item.setPurchaseList(this);
    }

    public void removeItem(PurchaseItem item) {
        items.remove(item);
        item.setPurchaseList(null);
    }
}
