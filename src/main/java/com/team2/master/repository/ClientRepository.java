package com.team2.master.repository;

import com.team2.master.entity.Client;
import com.team2.master.entity.enums.ClientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByClientCode(String clientCode);

    boolean existsByClientCode(String clientCode);

    List<Client> findByClientStatus(ClientStatus clientStatus);

    List<Client> findByDepartmentId(Integer departmentId);
}
