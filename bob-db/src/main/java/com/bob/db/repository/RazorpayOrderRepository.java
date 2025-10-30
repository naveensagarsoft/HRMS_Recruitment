package com.bob.db.repository;

import com.bob.db.entity.RazorpayOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RazorpayOrderRepository extends JpaRepository<RazorpayOrderEntity, Long> {
}
