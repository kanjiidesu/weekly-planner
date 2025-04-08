package com.weeklyPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
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

}
