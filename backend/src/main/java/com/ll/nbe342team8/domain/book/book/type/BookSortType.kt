package com.ll.nbe342team8.domain.book.book.type

import com.ll.nbe342team8.global.types.Sortable
import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Sort

@Getter
@RequiredArgsConstructor
enum class BookSortType(
    override val field: String,
    override val direction: Sort.Direction
) : Sortable {
    PUBLISHED_DATE("pubDate", Sort.Direction.DESC),  // 출간일순
    SALES_POINT("salesPoint", Sort.Direction.ASC),  // 판매량순
    RATING("averageRating", Sort.Direction.DESC),  // 평점순
    REVIEW_COUNT("reviewCount", Sort.Direction.DESC); // 리뷰 많은순

}
