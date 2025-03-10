package com.ll.nbe342team8.standard.PageDto;

import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDto;
import com.ll.nbe342team8.domain.qna.question.dto.QuestionListDtoProjection;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageDto<T> {
    @NonNull
    private final int currentPageNumber;
    @NonNull
    private final int pageSize;
    @NonNull
    private final long totalPages;
    @NonNull
    private final long totalItems;
    @NonNull
    private final List<T> items;

    public PageDto(Page<T> page) {
        this.currentPageNumber = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.items = page.getContent();
    }

    public PageDto(List<T> list) {
        this.currentPageNumber = 0;
        this.pageSize = 0;
        this.totalPages = 0;
        this.totalItems = 0;
        this.items = list;
    }

    public PageDto() {
        this.currentPageNumber = 0;
        this.pageSize = 0;
        this.totalPages = 0;
        this.totalItems = 0;
        this.items = new ArrayList<>();
    }
}
