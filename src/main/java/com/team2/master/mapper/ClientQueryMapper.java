package com.team2.master.mapper;

import com.team2.master.dto.ClientResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientQueryMapper {
    ClientResponse findById(@Param("clientId") Integer clientId);
    List<ClientResponse> findAll();
    List<ClientResponse> findByDepartmentId(@Param("departmentId") Integer departmentId);
    List<ClientResponse> findByClientStatus(@Param("clientStatus") String clientStatus);
}
