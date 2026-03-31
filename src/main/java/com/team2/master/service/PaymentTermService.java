package com.team2.master.service;

import com.team2.master.dto.CreatePaymentTermRequest;
import com.team2.master.dto.UpdatePaymentTermRequest;
import com.team2.master.entity.PaymentTerm;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.PaymentTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentTermService {

    private final PaymentTermRepository paymentTermRepository;

    public List<PaymentTerm> getAll() {
        return paymentTermRepository.findAll();
    }

    public PaymentTerm getById(Integer id) {
        return paymentTermRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("결제조건을 찾을 수 없습니다. ID: " + id));
    }

    @Transactional
    public PaymentTerm create(CreatePaymentTermRequest request) {
        if (paymentTermRepository.existsByPaymentTermCode(request.getPaymentTermCode())) {
            throw new IllegalStateException("이미 존재하는 결제조건 코드입니다: " + request.getPaymentTermCode());
        }
        PaymentTerm paymentTerm = new PaymentTerm(
                request.getPaymentTermCode(), request.getPaymentTermName(), request.getPaymentTermDescription()
        );
        return paymentTermRepository.save(paymentTerm);
    }

    @Transactional
    public PaymentTerm update(Integer id, UpdatePaymentTermRequest request) {
        PaymentTerm paymentTerm = getById(id);
        paymentTermRepository.findByPaymentTermCode(request.getPaymentTermCode())
                .filter(existing -> !existing.getPaymentTermId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("이미 존재하는 결제조건 코드입니다: " + request.getPaymentTermCode());
                });
        paymentTerm.update(request.getPaymentTermCode(), request.getPaymentTermName(), request.getPaymentTermDescription());
        return paymentTerm;
    }

    @Transactional
    public void delete(Integer id) {
        PaymentTerm paymentTerm = getById(id);
        paymentTermRepository.delete(paymentTerm);
    }
}
