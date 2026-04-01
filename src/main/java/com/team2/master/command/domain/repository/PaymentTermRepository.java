package com.team2.master.command.domain.repository;

import com.team2.master.command.domain.entity.PaymentTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentTermRepository extends JpaRepository<PaymentTerm, Integer> {

    Optional<PaymentTerm> findByPaymentTermCode(String paymentTermCode);

    boolean existsByPaymentTermCode(String paymentTermCode);
}
