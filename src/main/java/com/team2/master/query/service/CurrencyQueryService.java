package com.team2.master.query.service;

import com.team2.master.entity.Currency;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.CurrencyQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrencyQueryService {

    private final CurrencyQueryMapper currencyQueryMapper;

    public List<Currency> getAll() {
        return currencyQueryMapper.findAll();
    }

    public Currency getById(Integer id) {
        Currency currency = currencyQueryMapper.findById(id);
        if (currency == null) {
            throw new ResourceNotFoundException("통화를 찾을 수 없습니다. ID: " + id);
        }
        return currency;
    }
}
