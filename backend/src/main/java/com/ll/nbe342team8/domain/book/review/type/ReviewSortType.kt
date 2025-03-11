package com.ll.nbe342team8.domain.book.review.type

import com.ll.nbe342team8.global.types.Sortable
import org.springframework.data.domain.Sort

enum class ReviewSortType(
    override val field: String,
    override val direction: Sort.Direction
) : Sortable {
    CREATE_AT_DESC("createDate", Sort.Direction.DESC),  // 최근 등록순
    CREATE_AT_ASC("createDate", Sort.Direction.ASC),  // 과거 등록순
    RATING_DESC("rating", Sort.Direction.DESC),  // 평점 높은 순
    RATING_ASC("rating", Sort.Direction.ASC); // 평점 낮은 순
}
