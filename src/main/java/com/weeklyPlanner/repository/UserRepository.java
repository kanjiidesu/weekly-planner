package com.weeklyPlanner.repository;

import com.weeklyPlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Add this method to find the user by their username
    Optional<User> findByUsername(String username);
}
