package com.team2.master.service;

import com.team2.master.entity.Country;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.mapper.CountryQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CountryQueryService {

    private final CountryQueryMapper countryQueryMapper;

    public List<Country> getAll() {
        return countryQueryMapper.findAll();
    }

    public Country getById(Integer id) {
        Country country = countryQueryMapper.findById(id);
        if (country == null) {
            throw new ResourceNotFoundException("국가를 찾을 수 없습니다. ID: " + id);
        }
        return country;
    }
}
