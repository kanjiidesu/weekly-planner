package com.weeklyPlanner.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
// to prevent lazy loading, otherwise it gets error
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @ManyToMany(mappedBy = "users")
    private List<PurchaseList> purchaseLists;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    public User() {

    }

    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    public long getUserId(){return userId;}
    public void setUserId(long userId){this.userId = userId;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

}
