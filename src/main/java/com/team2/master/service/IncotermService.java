package com.team2.master.service;

import com.team2.master.dto.CreateIncotermRequest;
import com.team2.master.dto.UpdateIncotermRequest;
import com.team2.master.entity.Incoterm;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.IncotermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IncotermService {

    private final IncotermRepository incotermRepository;

    public List<Incoterm> getAll() {
        return incotermRepository.findAll();
    }

    public Incoterm getById(Integer id) {
        return incotermRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("인코텀을 찾을 수 없습니다. ID: " + id));
    }

    @Transactional
    public Incoterm create(CreateIncotermRequest request) {
        if (incotermRepository.existsByIncotermCode(request.getIncotermCode())) {
            throw new IllegalStateException("이미 존재하는 인코텀 코드입니다: " + request.getIncotermCode());
        }
        Incoterm incoterm = new Incoterm(
                request.getIncotermCode(), request.getIncotermName(), request.getIncotermNameKr(),
                request.getIncotermDescription(), request.getIncotermTransportMode(),
                request.getIncotermSellerSegments(), request.getIncotermDefaultNamedPlace()
        );
        return incotermRepository.save(incoterm);
    }

    @Transactional
    public Incoterm update(Integer id, UpdateIncotermRequest request) {
        Incoterm incoterm = getById(id);
        incotermRepository.findByIncotermCode(request.getIncotermCode())
                .filter(existing -> !existing.getIncotermId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("이미 존재하는 인코텀 코드입니다: " + request.getIncotermCode());
                });
        incoterm.update(
                request.getIncotermCode(), request.getIncotermName(), request.getIncotermNameKr(),
                request.getIncotermDescription(), request.getIncotermTransportMode(),
                request.getIncotermSellerSegments(), request.getIncotermDefaultNamedPlace()
        );
        return incoterm;
    }

    @Transactional
    public void delete(Integer id) {
        Incoterm incoterm = getById(id);
        incotermRepository.delete(incoterm);
    }
}
