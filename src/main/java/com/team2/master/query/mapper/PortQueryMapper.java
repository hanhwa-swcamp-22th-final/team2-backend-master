package com.team2.master.query.mapper;

import com.team2.master.query.dto.PortResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PortQueryMapper {
    PortResponse findById(@Param("portId") Integer portId);
    List<PortResponse> findAll();
}
