package com.weeklyPlanner.repository;

import com.weeklyPlanner.model.UserPurchaseList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPurchaseListRepository extends JpaRepository<UserPurchaseList, Long> {
}
