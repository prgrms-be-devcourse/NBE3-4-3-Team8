package com.ll.nbe342team8.domain.book.book.type;

import org.springframework.data.domain.Sort;

public enum SortType {
    PUBLISHED_DATE("pubDate", Sort.Direction.DESC),       // 출간일순
    SALES_POINT("salesPoint", Sort.Direction.ASC),              // 판매량순
    RATING("rating", Sort.Direction.DESC),                      // 평점순
    REVIEW_COUNT("reviewCount", Sort.Direction.DESC);           // 리뷰 많은순

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
