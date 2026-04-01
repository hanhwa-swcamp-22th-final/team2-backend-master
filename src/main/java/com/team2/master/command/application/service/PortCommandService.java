package com.team2.master.command.application.service;

import com.team2.master.command.application.dto.CreatePortRequest;
import com.team2.master.query.dto.PortResponse;
import com.team2.master.command.application.dto.UpdatePortRequest;
import com.team2.master.command.domain.entity.Country;
import com.team2.master.command.domain.entity.Port;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.command.domain.repository.CountryRepository;
import com.team2.master.command.domain.repository.PortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PortCommandService {

    private final PortRepository portRepository;
    private final CountryRepository countryRepository;

    @Transactional
    public PortResponse create(CreatePortRequest request) {
        if (portRepository.existsByPortCode(request.getPortCode())) {
            throw new IllegalStateException("이미 존재하는 항구 코드입니다: " + request.getPortCode());
        }
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다. ID: " + request.getCountryId()));
        Port port = new Port(request.getPortCode(), request.getPortName(), request.getPortCity(), country);
        Port saved = portRepository.save(port);
        return PortResponse.from(saved);
    }

    @Transactional
    public PortResponse update(Integer id, UpdatePortRequest request) {
        Port port = portRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("항구를 찾을 수 없습니다. ID: " + id));
        portRepository.findByPortCode(request.getPortCode())
                .filter(existing -> !existing.getPortId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("이미 존재하는 항구 코드입니다: " + request.getPortCode());
                });
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다. ID: " + request.getCountryId()));
        port.update(request.getPortCode(), request.getPortName(), request.getPortCity(), country);
        return PortResponse.from(port);
    }

    @Transactional
    public void delete(Integer id) {
        Port port = portRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("항구를 찾을 수 없습니다. ID: " + id));
        portRepository.delete(port);
    }
}
