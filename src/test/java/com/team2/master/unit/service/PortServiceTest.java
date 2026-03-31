package com.team2.master.unit.service;

import com.team2.master.dto.CreatePortRequest;
import com.team2.master.dto.PortResponse;
import com.team2.master.dto.UpdatePortRequest;
import com.team2.master.entity.Country;
import com.team2.master.entity.Port;
import com.team2.master.exception.ResourceNotFoundException;
import com.team2.master.query.mapper.PortQueryMapper;
import com.team2.master.command.repository.CountryRepository;
import com.team2.master.command.repository.PortRepository;
import com.team2.master.command.service.PortCommandService;
import com.team2.master.query.service.PortQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class PortServiceTest {

    @Nested
    @DisplayName("PortQueryService 테스트")
    class QueryServiceTest {

        @Mock
        private PortQueryMapper portQueryMapper;

        @InjectMocks
        private PortQueryService portQueryService;

        @Test
        @DisplayName("전체 항구 목록 조회 테스트")
        void getAll() {
            // given
            PortResponse response = PortResponse.builder()
                    .portCode("KRPUS")
                    .portName("Busan Port")
                    .portCity("Busan")
                    .countryName("South Korea")
                    .build();
            given(portQueryMapper.findAll()).willReturn(List.of(response));

            // when
            List<PortResponse> result = portQueryService.getAll();

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPortCode()).isEqualTo("KRPUS");
        }

        @Test
        @DisplayName("항구 ID로 조회 테스트")
        void getById() {
            // given
            PortResponse response = PortResponse.builder()
                    .portCode("KRPUS")
                    .portName("Busan Port")
                    .portCity("Busan")
                    .countryName("South Korea")
                    .build();
            given(portQueryMapper.findById(1)).willReturn(response);

            // when
            PortResponse result = portQueryService.getById(1);

            // then
            assertThat(result.getPortCode()).isEqualTo("KRPUS");
        }

        @Test
        @DisplayName("존재하지 않는 항구 ID 조회 시 예외 발생 테스트")
        void getById_notFound() {
            // given
            given(portQueryMapper.findById(999)).willReturn(null);

            // when & then
            assertThatThrownBy(() -> portQueryService.getById(999))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("PortCommandService 테스트")
    class CommandServiceTest {

        @Mock
        private PortRepository portRepository;

        @Mock
        private CountryRepository countryRepository;

        @InjectMocks
        private PortCommandService portCommandService;

        @Test
        @DisplayName("항구 생성 테스트")
        void create() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            CreatePortRequest request = new CreatePortRequest("KRPUS", "Busan Port", "Busan", 1);
            Port port = new Port("KRPUS", "Busan Port", "Busan", country);
            given(portRepository.existsByPortCode("KRPUS")).willReturn(false);
            given(countryRepository.findById(1)).willReturn(Optional.of(country));
            given(portRepository.save(any(Port.class))).willReturn(port);

            // when
            Port result = portCommandService.create(request);

            // then
            assertThat(result.getPortCode()).isEqualTo("KRPUS");
        }

        @Test
        @DisplayName("중복 항구 코드로 생성 시 예외 발생 테스트")
        void create_duplicateCode() {
            // given
            CreatePortRequest request = new CreatePortRequest("KRPUS", "Busan Port", "Busan", 1);
            given(portRepository.existsByPortCode("KRPUS")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> portCommandService.create(request))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("존재하지 않는 국가로 항구 생성 시 예외 발생 테스트")
        void create_countryNotFound() {
            // given
            CreatePortRequest request = new CreatePortRequest("KRPUS", "Busan Port", "Busan", 999);
            given(portRepository.existsByPortCode("KRPUS")).willReturn(false);
            given(countryRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> portCommandService.create(request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("항구 수정 테스트")
        void update() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            Country newCountry = new Country("JP", "Japan", "일본");
            Port port = new Port("KRPUS", "Busan Port", "Busan", country);
            ReflectionTestUtils.setField(port, "portId", 1);
            UpdatePortRequest request = new UpdatePortRequest("JPTYO", "Tokyo Port", "Tokyo", 2);
            given(portRepository.findById(1)).willReturn(Optional.of(port));
            given(portRepository.findByPortCode("JPTYO")).willReturn(Optional.empty());
            given(countryRepository.findById(2)).willReturn(Optional.of(newCountry));

            // when
            Port result = portCommandService.update(1, request);

            // then
            assertThat(result.getPortCode()).isEqualTo("JPTYO");
            assertThat(result.getPortName()).isEqualTo("Tokyo Port");
        }

        @Test
        @DisplayName("항구 수정 시 중복 코드 예외 발생 테스트")
        void update_duplicateCode() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            Port port = new Port("KRPUS", "Busan Port", "Busan", country);
            ReflectionTestUtils.setField(port, "portId", 1);
            Port existing = new Port("JPTYO", "Tokyo Port", "Tokyo", country);
            ReflectionTestUtils.setField(existing, "portId", 2);
            UpdatePortRequest request = new UpdatePortRequest("JPTYO", "Tokyo Port Updated", "Tokyo", 1);
            given(portRepository.findById(1)).willReturn(Optional.of(port));
            given(portRepository.findByPortCode("JPTYO")).willReturn(Optional.of(existing));

            // when & then
            assertThatThrownBy(() -> portCommandService.update(1, request))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("존재하지 않는 국가로 항구 수정 시 예외 발생 테스트")
        void update_countryNotFound() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            Port port = new Port("KRPUS", "Busan Port", "Busan", country);
            ReflectionTestUtils.setField(port, "portId", 1);
            UpdatePortRequest request = new UpdatePortRequest("JPTYO", "Tokyo Port", "Tokyo", 999);
            given(portRepository.findById(1)).willReturn(Optional.of(port));
            given(portRepository.findByPortCode("JPTYO")).willReturn(Optional.empty());
            given(countryRepository.findById(999)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> portCommandService.update(1, request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("국가를 찾을 수 없습니다");
        }

        @Test
        @DisplayName("항구 삭제 테스트")
        void delete() {
            // given
            Country country = new Country("KR", "South Korea", "대한민국");
            Port port = new Port("KRPUS", "Busan Port", "Busan", country);
            given(portRepository.findById(1)).willReturn(Optional.of(port));

            // when
            portCommandService.delete(1);

            // then
            then(portRepository).should().delete(port);
        }
    }
}
