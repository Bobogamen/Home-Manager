package com.home_manager.repository;

import com.home_manager.model.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {

    List<Payment> findPaymentByMonthHomesId(long monthHomesId);
}
