package com.team2.master.mapper;

import com.team2.master.entity.Incoterm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IncotermQueryMapper {
    Incoterm findById(@Param("incotermId") Integer incotermId);
    List<Incoterm> findAll();
}
