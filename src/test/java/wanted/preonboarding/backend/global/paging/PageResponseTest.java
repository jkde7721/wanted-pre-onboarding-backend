package wanted.preonboarding.backend.global.paging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PageResponseTest {

    Page<Integer> page;
    PageResponse<Integer> pageResponse;

    @BeforeEach
    void init() {
        page = new PageImpl<>(List.of(1, 2, 3), PageRequest.of(0, 10), 30);
        pageResponse = new PageResponse<>(page);
    }

    @DisplayName("스프링 Page 객체로부터 생성 테스트")
    @Test
    void 스프링_Page_객체로부터_생성_테스트() {
        assertThat(pageResponse.getContent()).isEqualTo(page.getContent());
        assertThat(pageResponse.getPageNumber()).isEqualTo(page.getPageable().getPageNumber() + 1);
        assertThat(pageResponse.getPageSize()).isEqualTo(page.getPageable().getPageSize());
        assertThat(pageResponse.getNumberOfElements()).isEqualTo(page.getNumberOfElements());
        assertThat(pageResponse.getTotalPages()).isEqualTo(page.getTotalPages());
        assertThat(pageResponse.getTotalElements()).isEqualTo(page.getTotalElements());
        assertThat(pageResponse.getFirst()).isEqualTo(page.isFirst());
        assertThat(pageResponse.getLast()).isEqualTo(page.isLast());
        assertThat(pageResponse.getEmpty()).isEqualTo(page.isEmpty());
    }

    @DisplayName("DTO 객체로 변환 테스트")
    @Test
    void DTO_객체로_변환_테스트() {
        PageResponse<Integer> dtoPageResponse = pageResponse.toDto(i -> 0);

        assertThat(dtoPageResponse.getContent().size()).isEqualTo(pageResponse.getContent().size());
        assertThat(dtoPageResponse.getContent()).allMatch(c -> c == 0);
        assertThat(dtoPageResponse.getPageNumber()).isEqualTo(pageResponse.getPageNumber());
        assertThat(dtoPageResponse.getPageSize()).isEqualTo(pageResponse.getPageSize());
        assertThat(dtoPageResponse.getNumberOfElements()).isEqualTo(pageResponse.getNumberOfElements());
        assertThat(dtoPageResponse.getTotalPages()).isEqualTo(pageResponse.getTotalPages());
        assertThat(dtoPageResponse.getTotalElements()).isEqualTo(pageResponse.getTotalElements());
        assertThat(dtoPageResponse.getFirst()).isEqualTo(pageResponse.getFirst());
        assertThat(dtoPageResponse.getLast()).isEqualTo(pageResponse.getLast());
        assertThat(dtoPageResponse.getEmpty()).isEqualTo(pageResponse.getEmpty());
    }
}