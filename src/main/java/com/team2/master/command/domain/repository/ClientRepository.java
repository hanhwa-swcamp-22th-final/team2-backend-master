package com.team2.master.command.domain.repository;

import com.team2.master.command.domain.entity.Client;
import com.team2.master.command.domain.entity.enums.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByClientCode(String clientCode);

    boolean existsByClientCode(String clientCode);

    List<Client> findByClientStatus(ClientStatus clientStatus);

    List<Client> findByTeamId(Integer teamId);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.country LEFT JOIN FETCH c.port LEFT JOIN FETCH c.paymentTerm LEFT JOIN FETCH c.currency")
    List<Client> findAllWithRelations();

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.country LEFT JOIN FETCH c.port LEFT JOIN FETCH c.paymentTerm LEFT JOIN FETCH c.currency WHERE c.clientId = :id")
    Optional<Client> findByIdWithRelations(Integer id);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.country LEFT JOIN FETCH c.port LEFT JOIN FETCH c.paymentTerm LEFT JOIN FETCH c.currency WHERE c.teamId = :teamId")
    List<Client> findByTeamIdWithRelations(Integer teamId);

    @Query(value = "SELECT COALESCE(MAX(CAST(SUBSTRING(client_code, 4) AS UNSIGNED)), 0) " +
            "FROM clients WHERE client_code LIKE 'CLI%'", nativeQuery = true)
    Integer findMaxClientCodeNumber();
}
