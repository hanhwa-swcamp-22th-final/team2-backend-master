package com.team2.master.query.mapper;

import com.team2.master.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemQueryMapper {
    Item findById(@Param("itemId") Integer itemId);
    List<Item> findAll();
    List<Item> findByItemStatus(@Param("itemStatus") String itemStatus);
}
