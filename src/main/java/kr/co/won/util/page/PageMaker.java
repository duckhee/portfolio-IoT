package kr.co.won.util.page;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = {"pageList"})
public class PageMaker<T> {

    private Page<T> resultPage;
    private Pageable prevPage;
    private Pageable nextPage;
    private Pageable currentPage;

    private int totalPageNumber;
    private int currentPageNumber;
    private List<Pageable> pageList;

    public PageMaker(Page<T> resultPage) {
        this.resultPage = resultPage;
        this.currentPage = resultPage.getPageable();
        this.currentPageNumber = resultPage.getTotalPages();
        this.pageList = new ArrayList<>();

        calcPage();
    }

    private void calcPage() {
        int tempEndNumber = (int) (Math.ceil(this.currentPageNumber / 10.0) * 10);
        int startNumber = tempEndNumber - 9;

        Pageable startPage = this.currentPage;

        for (int i = startNumber; i < this.currentPageNumber; i++) {
            startPage = startPage.previousOrFirst();
        }

        this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();

        if (this.totalPageNumber < tempEndNumber) {
            tempEndNumber = this.totalPageNumber;
            this.nextPage = null;
        }

        for (int i = startNumber; i < tempEndNumber; i++) {
            pageList.add(startPage);
            startPage = startPage.next();
        }

        this.nextPage = startPage.getPageNumber() < totalPageNumber ? startPage : null;
    }

}
