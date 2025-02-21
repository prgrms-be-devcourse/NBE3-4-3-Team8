package com.ll.nbe342team8.global.types

import org.springframework.data.domain.Sort

interface Sortable {
    val field: String
    val direction: Sort.Direction

    val order: Sort.Order
        get() = Sort.Order(this.direction, this.field)
}
