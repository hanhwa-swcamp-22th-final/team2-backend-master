package com.team2.master.query.mapper;

import com.team2.master.command.domain.entity.Item;
import com.team2.master.query.dto.ItemListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemQueryMapper {
    Item findById(@Param("itemId") Integer itemId);
    List<Item> findAll();
    List<Item> findByItemStatus(@Param("itemStatus") String itemStatus);

    List<ItemListResponse> findByCondition(@Param("itemName") String itemName,
                                           @Param("itemCategory") String itemCategory,
                                           @Param("itemStatus") String itemStatus,
                                           @Param("size") int size,
                                           @Param("offset") int offset);

    long countByCondition(@Param("itemName") String itemName,
                          @Param("itemCategory") String itemCategory,
                          @Param("itemStatus") String itemStatus);
}
