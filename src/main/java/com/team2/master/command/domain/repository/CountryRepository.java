package com.team2.master.command.domain.repository;

import com.team2.master.command.domain.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    Optional<Country> findByCountryCode(String countryCode);

    boolean existsByCountryCode(String countryCode);
}
