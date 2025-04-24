package com.weeklyPlanner.repository;

import com.weeklyPlanner.model.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

}
