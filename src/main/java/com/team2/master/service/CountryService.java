package com.team2.master.service;

import com.team2.master.dto.CreateCountryRequest;
import com.team2.master.dto.UpdateCountryRequest;
import com.team2.master.entity.Country;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CountryService {

    private final CountryRepository countryRepository;

    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    public Country getById(Integer id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다. ID: " + id));
    }

    @Transactional
    public Country create(CreateCountryRequest request) {
        if (countryRepository.existsByCountryCode(request.getCountryCode())) {
            throw new IllegalStateException("이미 존재하는 국가 코드입니다: " + request.getCountryCode());
        }
        Country country = new Country(request.getCountryCode(), request.getCountryName(), request.getCountryNameKr());
        return countryRepository.save(country);
    }

    @Transactional
    public Country update(Integer id, UpdateCountryRequest request) {
        Country country = getById(id);
        countryRepository.findByCountryCode(request.getCountryCode())
                .filter(existing -> !existing.getCountryId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("이미 존재하는 국가 코드입니다: " + request.getCountryCode());
                });
        country.update(request.getCountryCode(), request.getCountryName(), request.getCountryNameKr());
        return country;
    }

    @Transactional
    public void delete(Integer id) {
        Country country = getById(id);
        countryRepository.delete(country);
    }
}
