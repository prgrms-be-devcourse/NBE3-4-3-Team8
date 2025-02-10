package com.ll.nbe342team8.global.types;

import org.springframework.data.domain.Sort;

public interface Sortable {
    String getField();
    Sort.Direction getDirection();

    default Sort.Order getOrder() {
        return new Sort.Order(getDirection(), getField());
    }
}
