package com.team2.master.unit.service;

import com.team2.master.dto.CreateIncotermRequest;
import com.team2.master.dto.UpdateIncotermRequest;
import com.team2.master.entity.Incoterm;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.repository.IncotermRepository;
import com.team2.master.service.IncotermService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class IncotermServiceTest {

    @Mock
    private IncotermRepository incotermRepository;

    @InjectMocks
    private IncotermService incotermService;

    @Test
    @DisplayName("전체 인코텀 목록 조회 테스트")
    void getAll() {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, "Sea", null, null);
        given(incotermRepository.findAll()).willReturn(List.of(incoterm));

        // when
        List<Incoterm> result = incotermService.getAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIncotermCode()).isEqualTo("FOB");
    }

    @Test
    @DisplayName("인코텀 ID로 조회 테스트")
    void getById() {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, null, null, null);
        given(incotermRepository.findById(1)).willReturn(Optional.of(incoterm));

        // when
        Incoterm result = incotermService.getById(1);

        // then
        assertThat(result.getIncotermCode()).isEqualTo("FOB");
    }

    @Test
    @DisplayName("존재하지 않는 인코텀 ID 조회 시 예외 발생 테스트")
    void getById_notFound() {
        // given
        given(incotermRepository.findById(999)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> incotermService.getById(999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("인코텀 생성 테스트")
    void create() {
        // given
        CreateIncotermRequest request = new CreateIncotermRequest("FOB", "Free On Board", "본선인도",
                "desc", "Sea", "E", "Port");
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                "desc", "Sea", "E", "Port");
        given(incotermRepository.existsByIncotermCode("FOB")).willReturn(false);
        given(incotermRepository.save(any(Incoterm.class))).willReturn(incoterm);

        // when
        Incoterm result = incotermService.create(request);

        // then
        assertThat(result.getIncotermCode()).isEqualTo("FOB");
    }

    @Test
    @DisplayName("중복 인코텀 코드로 생성 시 예외 발생 테스트")
    void create_duplicateCode() {
        // given
        CreateIncotermRequest request = new CreateIncotermRequest("FOB", "Free On Board", null,
                null, null, null, null);
        given(incotermRepository.existsByIncotermCode("FOB")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> incotermService.create(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("인코텀 수정 테스트")
    void update() {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, null, null, null);
        ReflectionTestUtils.setField(incoterm, "incotermId", 1);
        UpdateIncotermRequest request = new UpdateIncotermRequest("FOB", "FOB Updated", "본선인도수정",
                "desc", "Any", "F", "Place");
        given(incotermRepository.findById(1)).willReturn(Optional.of(incoterm));
        given(incotermRepository.findByIncotermCode("FOB")).willReturn(Optional.of(incoterm));

        // when
        Incoterm result = incotermService.update(1, request);

        // then
        assertThat(result.getIncotermName()).isEqualTo("FOB Updated");
    }

    @Test
    @DisplayName("인코텀 수정 시 중복 코드 예외 발생 테스트")
    void update_duplicateCode() {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, null, null, null);
        ReflectionTestUtils.setField(incoterm, "incotermId", 1);
        Incoterm existing = new Incoterm("CIF", "Cost Insurance Freight", "운임보험료포함",
                null, null, null, null);
        ReflectionTestUtils.setField(existing, "incotermId", 2);
        UpdateIncotermRequest request = new UpdateIncotermRequest("CIF", "CIF Updated", "수정",
                null, null, null, null);
        given(incotermRepository.findById(1)).willReturn(Optional.of(incoterm));
        given(incotermRepository.findByIncotermCode("CIF")).willReturn(Optional.of(existing));

        // when & then
        assertThatThrownBy(() -> incotermService.update(1, request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("인코텀 삭제 테스트")
    void delete() {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, null, null, null);
        given(incotermRepository.findById(1)).willReturn(Optional.of(incoterm));

        // when
        incotermService.delete(1);

        // then
        then(incotermRepository).should().delete(incoterm);
    }
}
