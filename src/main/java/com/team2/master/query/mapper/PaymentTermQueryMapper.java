package com.team2.master.query.mapper;

import com.team2.master.entity.PaymentTerm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentTermQueryMapper {
    PaymentTerm findById(@Param("paymentTermId") Integer paymentTermId);
    List<PaymentTerm> findAll();
}
