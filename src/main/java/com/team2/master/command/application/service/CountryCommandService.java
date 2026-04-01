package com.team2.master.command.application.service;

import com.team2.master.command.application.dto.CreateCountryRequest;
import com.team2.master.command.application.dto.UpdateCountryRequest;
import com.team2.master.command.domain.entity.Country;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.domain.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountryCommandService {

    private final CountryRepository countryRepository;

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
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다. ID: " + id));
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
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다. ID: " + id));
        countryRepository.delete(country);
    }
}
