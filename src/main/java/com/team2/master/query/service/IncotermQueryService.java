package com.team2.master.query.service;

import com.team2.master.entity.Incoterm;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.IncotermQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IncotermQueryService {

    private final IncotermQueryMapper incotermQueryMapper;

    public List<Incoterm> getAll() {
        return incotermQueryMapper.findAll();
    }

    public Incoterm getById(Integer id) {
        Incoterm incoterm = incotermQueryMapper.findById(id);
        if (incoterm == null) {
            throw new ResourceNotFoundException("인코텀을 찾을 수 없습니다. ID: " + id);
        }
        return incoterm;
    }
}
