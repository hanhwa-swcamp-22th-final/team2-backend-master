package com.team2.master.repository;

import com.team2.master.entity.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PortRepository extends JpaRepository<Port, Integer> {

    Optional<Port> findByPortCode(String portCode);

    boolean existsByPortCode(String portCode);

    List<Port> findByCountryCountryId(Integer countryId);

    @Query("SELECT p FROM Port p JOIN FETCH p.country")
    List<Port> findAllWithCountry();
}
