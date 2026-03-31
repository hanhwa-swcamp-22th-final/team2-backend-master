package com.team2.master.query.mapper;

import com.team2.master.dto.BuyerResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BuyerQueryMapper {
    BuyerResponse findById(@Param("buyerId") Integer buyerId);
    List<BuyerResponse> findAll();
    List<BuyerResponse> findByClientId(@Param("clientId") Integer clientId);
}
