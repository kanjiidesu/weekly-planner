package com.weeklyPlanner.repository;

import com.weeklyPlanner.model.PurchaseList;
import com.weeklyPlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseListRepository extends JpaRepository<PurchaseList, Long> {

    Optional<PurchaseList> findByPurchaseListNameAndUsersContaining(String purchaseListName, User user);
    List<PurchaseList> findAllByUsersContaining(User user);
    List<PurchaseList> findByPurchaseListName(String purchaseListName);
}
