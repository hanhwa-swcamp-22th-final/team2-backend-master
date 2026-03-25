package com.team2.master.repository;

import com.team2.master.entity.Port;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortRepository extends JpaRepository<Port, Integer> {

    Optional<Port> findByPortCode(String portCode);

    boolean existsByPortCode(String portCode);

    List<Port> findByCountryId(Integer countryId);
}
