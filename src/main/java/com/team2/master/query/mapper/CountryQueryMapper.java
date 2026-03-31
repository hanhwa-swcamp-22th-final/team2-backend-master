package com.team2.master.query.mapper;

import com.team2.master.entity.Country;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CountryQueryMapper {
    Country findById(@Param("countryId") Integer countryId);
    List<Country> findAll();
}
