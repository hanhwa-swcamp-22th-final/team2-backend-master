package com.team2.master.command.domain.repository;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.command.domain.entity.enums.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findByItemCode(String itemCode);

    boolean existsByItemCode(String itemCode);

    List<Item> findByItemStatus(ItemStatus itemStatus);

    @Query(value = "SELECT COALESCE(MAX(CAST(SUBSTRING(item_code, 4) AS UNSIGNED)), 0) " +
            "FROM items WHERE item_code LIKE 'ITM%'", nativeQuery = true)
    Integer findMaxItemCodeNumber();
}
