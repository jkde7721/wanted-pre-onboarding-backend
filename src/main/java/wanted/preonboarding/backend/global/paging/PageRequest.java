package wanted.preonboarding.backend.global.paging;

public class PageRequest {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 30;
    private int page = DEFAULT_PAGE;
    private int size = DEFAULT_SIZE;

    //one-based page number, must be greater than 0.
    public void setPage(int page) {
        if (page > 0) this.page = page;
    }

    //the size of the page, must be greater than 0.
    public void setSize(int size) {
        if (size > 0) this.size = Math.min(size, MAX_SIZE);
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size);
    }
}
