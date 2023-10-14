package wanted.preonboarding.backend.global.paging;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

class PageRequestTest {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 30;
    PageRequest pageRequest = new PageRequest();

    @DisplayName("페이징 기본값 테스트")
    @Test
    void 페이징_기본값_테스트() {
        assertThat(pageRequest.getPage()).isEqualTo(DEFAULT_PAGE);
        assertThat(pageRequest.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @DisplayName("페이지 번호 0 이하 테스트")
    @ValueSource(ints = {-1, -5, 0})
    @ParameterizedTest
    void 페이지_번호_0_이하_테스트(int page) {
        pageRequest.setPage(page);
        assertThat(pageRequest.getPage()).isEqualTo(DEFAULT_PAGE);
    }

    @DisplayName("페이지 사이즈 0 이하 테스트")
    @ValueSource(ints = {-1, -5, 0})
    @ParameterizedTest
    void 페이지_사이즈_0_이하_테스트(int size) {
        pageRequest.setSize(size);
        assertThat(pageRequest.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @DisplayName("페이지 사이즈 최대값 초과 테스트")
    @ValueSource(ints = {31, 50, 1000000000})
    @ParameterizedTest
    void 페이지_사이즈_최대값_초과_테스트(int size) {
        pageRequest.setSize(size);
        assertThat(pageRequest.getSize()).isEqualTo(MAX_SIZE);
    }

    @DisplayName("스프링 PageRequest 객체 생성 테스트")
    @MethodSource("pagingArgsProvider")
    @ParameterizedTest
    void 스프링_PageRequest_객체_생성_테스트(int page, int size) {
        pageRequest.setPage(page);
        pageRequest.setSize(size);
        org.springframework.data.domain.PageRequest realPageRequest = pageRequest.of();

        assertThat(realPageRequest.getPageNumber()).isEqualTo(pageRequest.getPage() - 1);
        assertThat(realPageRequest.getPageSize()).isEqualTo(pageRequest.getSize());
    }

    static Stream<Arguments> pagingArgsProvider() {
        return Stream.of(arguments(1, 10), arguments(3, 30), arguments(-1, 1000000000));
    }
}