package com.team2.master.service;

import com.team2.master.dto.CreatePortRequest;
import com.team2.master.dto.UpdatePortRequest;
import com.team2.master.entity.Country;
import com.team2.master.entity.Port;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.CountryRepository;
import com.team2.master.repository.PortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortService {

    private final PortRepository portRepository;
    private final CountryRepository countryRepository;

    public List<Port> getAll() {
        return portRepository.findAllWithCountry();
    }

    public Port getById(Integer id) {
        return portRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("항구를 찾을 수 없습니다. ID: " + id));
    }

    @Transactional
    public Port create(CreatePortRequest request) {
        if (portRepository.existsByPortCode(request.getPortCode())) {
            throw new IllegalStateException("이미 존재하는 항구 코드입니다: " + request.getPortCode());
        }
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다. ID: " + request.getCountryId()));
        Port port = new Port(request.getPortCode(), request.getPortName(), request.getPortCity(), country);
        return portRepository.save(port);
    }

    @Transactional
    public Port update(Integer id, UpdatePortRequest request) {
        Port port = getById(id);
        portRepository.findByPortCode(request.getPortCode())
                .filter(existing -> !existing.getPortId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("이미 존재하는 항구 코드입니다: " + request.getPortCode());
                });
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("국가를 찾을 수 없습니다. ID: " + request.getCountryId()));
        port.update(request.getPortCode(), request.getPortName(), request.getPortCity(), country);
        return port;
    }

    @Transactional
    public void delete(Integer id) {
        Port port = getById(id);
        portRepository.delete(port);
    }
}
