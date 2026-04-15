package com.team2.master.query.mapper;

import com.team2.master.query.dto.BuyerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BuyerQueryMapper {
    BuyerResponse findById(@Param("buyerId") Integer buyerId);
    List<BuyerResponse> findAll();
    List<BuyerResponse> findByClientId(@Param("clientId") Integer clientId);

    List<BuyerResponse> findAllPage(@Param("size") int size, @Param("offset") int offset);
    long countAll();

    List<BuyerResponse> findByClientIdPage(@Param("clientId") Integer clientId,
                                           @Param("size") int size,
                                           @Param("offset") int offset);
    long countByClientId(@Param("clientId") Integer clientId);
}
