package com.team2.master.query.mapper;

import com.team2.master.entity.Currency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CurrencyQueryMapper {
    Currency findById(@Param("currencyId") Integer currencyId);
    List<Currency> findAll();
}
