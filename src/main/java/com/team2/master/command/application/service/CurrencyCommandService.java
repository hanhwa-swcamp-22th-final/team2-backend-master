package com.team2.master.command.application.service;

import com.team2.master.command.application.dto.CreateCurrencyRequest;
import com.team2.master.command.application.dto.UpdateCurrencyRequest;
import com.team2.master.command.domain.entity.Currency;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.domain.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrencyCommandService {

    private final CurrencyRepository currencyRepository;

    @Transactional
    public Currency create(CreateCurrencyRequest request) {
        if (currencyRepository.existsByCurrencyCode(request.getCurrencyCode())) {
            throw new IllegalStateException("이미 존재하는 통화 코드입니다: " + request.getCurrencyCode());
        }
        Currency currency = new Currency(request.getCurrencyCode(), request.getCurrencyName(), request.getCurrencySymbol());
        return currencyRepository.save(currency);
    }

    @Transactional
    public Currency update(Integer id, UpdateCurrencyRequest request) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("통화를 찾을 수 없습니다. ID: " + id));
        currencyRepository.findByCurrencyCode(request.getCurrencyCode())
                .filter(existing -> !existing.getCurrencyId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("이미 존재하는 통화 코드입니다: " + request.getCurrencyCode());
                });
        currency.update(request.getCurrencyCode(), request.getCurrencyName(), request.getCurrencySymbol());
        return currency;
    }

    @Transactional
    public void delete(Integer id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("통화를 찾을 수 없습니다. ID: " + id));
        currencyRepository.delete(currency);
    }
}
