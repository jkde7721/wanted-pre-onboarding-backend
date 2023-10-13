package wanted.preonboarding.backend.global.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer numberOfElements;
    private Integer totalPages;
    private Long totalElements;
    private Boolean first;
    private Boolean last;
    private Boolean empty;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getPageable().getPageNumber() + 1; //페이지 번호는 1부터 시작
        this.pageSize = page.getPageable().getPageSize();
        this.numberOfElements = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.empty = page.isEmpty();
    }

    public <R> PageResponse<R> toDto(Function<T, R> function) {
        List<R> dtoContent = content.stream().map(function).collect(Collectors.toList());
        return new PageResponse<>(dtoContent, pageNumber, pageSize, numberOfElements, totalPages, totalElements, first, last, empty);
    }
}
