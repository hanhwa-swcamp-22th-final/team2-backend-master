package com.team2.master.query.service;

import com.team2.master.command.domain.entity.PaymentTerm;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.PaymentTermQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentTermQueryService {

    private final PaymentTermQueryMapper paymentTermQueryMapper;

    public List<PaymentTerm> getAll() {
        return paymentTermQueryMapper.findAll();
    }

    public PaymentTerm getById(Integer id) {
        PaymentTerm paymentTerm = paymentTermQueryMapper.findById(id);
        if (paymentTerm == null) {
            throw new ResourceNotFoundException("결제조건을 찾을 수 없습니다. ID: " + id);
        }
        return paymentTerm;
    }
}
