package com.team2.master.service;

import com.team2.master.dto.CreateCurrencyRequest;
import com.team2.master.dto.UpdateCurrencyRequest;
import com.team2.master.entity.Currency;
import com.team2.master.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public List<Currency> getAll() {
        return currencyRepository.findAll();
    }

    public Currency getById(Integer id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("통화를 찾을 수 없습니다. ID: " + id));
    }

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
        Currency currency = getById(id);
        currency.update(request.getCurrencyCode(), request.getCurrencyName(), request.getCurrencySymbol());
        return currency;
    }

    @Transactional
    public void delete(Integer id) {
        Currency currency = getById(id);
        currencyRepository.delete(currency);
    }
}
