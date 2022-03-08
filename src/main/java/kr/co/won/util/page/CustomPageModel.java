package kr.co.won.util.page;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

import java.util.List;

public class CustomPageModel<T> extends PagedModel<T> {

    @JsonUnwrapped
    Page page;

    public CustomPageModel(Page<T> page) {
        long totalElements = page.getTotalElements();
        int totalPages = page.getTotalPages();
        Pageable pageable = page.getPageable();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        PageMetadata pageMetadata = new PageMetadata(pageSize, pageNumber, totalElements);
        List<T> content = page.getContent();
        PagedModel.of(content, pageMetadata);
    }

}
