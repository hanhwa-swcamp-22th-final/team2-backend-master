package com.team2.master.command.repository;

import com.team2.master.entity.Incoterm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncotermRepository extends JpaRepository<Incoterm, Integer> {

    Optional<Incoterm> findByIncotermCode(String incotermCode);

    boolean existsByIncotermCode(String incotermCode);
}
