package kr.co.won.util.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageDto  {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_MAX_PAGE_SIZE = 50;

    private int page;
    private int size;

    private String keyword;
    private String type;

    /** Default page constructor */
    public PageDto() {
//        this.page = 0;
        this.page = 1;
        this.size = DEFAULT_PAGE_SIZE;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
//        this.page = page < 0 ? 0 : page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size < DEFAULT_PAGE_SIZE || size > DEFAULT_MAX_PAGE_SIZE ? DEFAULT_PAGE_SIZE : size;
    }

    public Pageable makePageable(int direction, String... props) {
        Sort.Direction dir = direction == 0 ? Sort.Direction.DESC : Sort.Direction.DESC;
        return PageRequest.of(this.page - 1, this.size, Sort.by(dir, props));
//        return PageRequest.of(this.page, this.size, Sort.by(dir, props));
    }

}
