package com.team2.master.integration.repository;

import com.team2.master.entity.Incoterm;
import com.team2.master.command.repository.IncotermRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IncotermRepositoryTest {

    @Autowired
    private IncotermRepository incotermRepository;

    @Test
    @DisplayName("인코텀 코드로 조회 테스트")
    void findByIncotermCode() {
        // given
        Incoterm incoterm = new Incoterm("FOB", "Free On Board", "본선인도",
                null, "Sea", null, null);
        incotermRepository.save(incoterm);

        // when
        Optional<Incoterm> found = incotermRepository.findByIncotermCode("FOB");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getIncotermName()).isEqualTo("Free On Board");
    }

    @Test
    @DisplayName("존재하지 않는 인코텀 코드 조회 테스트")
    void findByIncotermCode_notFound() {
        // when
        Optional<Incoterm> found = incotermRepository.findByIncotermCode("XXX");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("인코텀 코드 존재 여부 확인 테스트")
    void existsByIncotermCode() {
        // given
        Incoterm incoterm = new Incoterm("CIF", "Cost Insurance and Freight", "운임보험료포함인도",
                null, null, null, null);
        incotermRepository.save(incoterm);

        // when & then
        assertThat(incotermRepository.existsByIncotermCode("CIF")).isTrue();
        assertThat(incotermRepository.existsByIncotermCode("XXX")).isFalse();
    }
}
