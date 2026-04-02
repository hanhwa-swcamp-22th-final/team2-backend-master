package com.team2.master.query.mapper;

import com.team2.master.query.dto.ClientListResponse;
import com.team2.master.query.dto.ClientResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientQueryMapper {
    ClientResponse findById(@Param("clientId") Integer clientId);
    List<ClientResponse> findAll();
    List<ClientResponse> findByDepartmentId(@Param("departmentId") Integer departmentId);
    List<ClientResponse> findByClientStatus(@Param("clientStatus") String clientStatus);

    List<ClientListResponse> findByCondition(@Param("clientName") String clientName,
                                             @Param("countryId") Integer countryId,
                                             @Param("clientStatus") String clientStatus,
                                             @Param("departmentId") Integer departmentId,
                                             @Param("size") int size,
                                             @Param("offset") int offset);

    long countByCondition(@Param("clientName") String clientName,
                          @Param("countryId") Integer countryId,
                          @Param("clientStatus") String clientStatus,
                          @Param("departmentId") Integer departmentId);
}
