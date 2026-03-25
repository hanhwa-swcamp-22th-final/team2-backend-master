package com.team2.master.repository;

import com.team2.master.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyerRepository extends JpaRepository<Buyer, Integer> {

    List<Buyer> findByClientId(Integer clientId);
}
