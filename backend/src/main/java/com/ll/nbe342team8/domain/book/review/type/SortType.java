package com.ll.nbe342team8.domain.book.review.type;

import org.springframework.data.domain.Sort;

public enum SortType {
    CREATE_AT_DESC("createDate", Sort.Direction.DESC), // 최근 등록순
    CREATE_AT_ASC("createDate", Sort.Direction.ASC),   // 과거 등록순
    RATING_DESC("rating", Sort.Direction.DESC),      // 평점 높은 순
    RATING_ASC("rating", Sort.Direction.ASC);         // 평점 낮은 순

    private final String field;
    private final Sort.Direction direction;

    SortType(String field, Sort.Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public Sort.Order getOrder() {
        return new Sort.Order(direction, field);
    }
}
