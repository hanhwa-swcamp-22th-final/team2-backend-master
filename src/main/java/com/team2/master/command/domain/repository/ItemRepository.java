package com.team2.master.command.domain.repository;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByItemCode(String itemCode);

    boolean existsByItemCode(String itemCode);

    List<Item> findByItemStatus(ItemStatus itemStatus);
}
