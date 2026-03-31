package com.team2.master.service;

import com.team2.master.dto.CreateIncotermRequest;
import com.team2.master.dto.UpdateIncotermRequest;
import com.team2.master.entity.Incoterm;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.IncotermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncotermCommandService {

    private final IncotermRepository incotermRepository;

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
        Incoterm incoterm = incotermRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("인코텀을 찾을 수 없습니다. ID: " + id));
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
        Incoterm incoterm = incotermRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("인코텀을 찾을 수 없습니다. ID: " + id));
        incotermRepository.delete(incoterm);
    }
}
