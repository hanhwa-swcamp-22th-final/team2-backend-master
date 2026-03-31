package com.team2.master.command.repository;

import com.team2.master.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BuyerRepository extends JpaRepository<Buyer, Integer> {

    List<Buyer> findByClientClientId(Integer clientId);

    @Query("SELECT b FROM Buyer b JOIN FETCH b.client")
    List<Buyer> findAllWithClient();

    @Query("SELECT b FROM Buyer b JOIN FETCH b.client WHERE b.buyerId = :id")
    Optional<Buyer> findByIdWithClient(Integer id);

    @Query("SELECT b FROM Buyer b JOIN FETCH b.client WHERE b.client.clientId = :clientId")
    List<Buyer> findByClientIdWithClient(Integer clientId);
}
