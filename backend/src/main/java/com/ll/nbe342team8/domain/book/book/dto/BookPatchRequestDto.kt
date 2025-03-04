package com.ll.nbe342team8.domain.book.book.dto

import com.ll.nbe342team8.domain.book.category.entity.Category
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import java.time.LocalDate

data class BookPatchRequestDto(
    var title: String? = null,
    var author: String? = null,

    @field:Pattern(regexp = "^[0-9]{10}$", message = "ISBN은 10자리 숫자여야 합니다.")
    var isbn: String? = null, // 10자리 숫자만 허용

    @field:Pattern(regexp = "^[0-9]{13}$", message = "ISBN13은 13자리 숫자여야 합니다.")
    var isbn13: String? = null, // 13자리 숫자만 허용

    var pubDate: LocalDate? = null,

    @field:Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    var priceStandard: Int? = null,

    @field:Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    var priceSales: Int? = null,

    @field:Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    var stock: Int? = null,

    var status: Int? = null,

    @field:DecimalMin(value = "0.0", message = "평점은 0 이상이어야 합니다.")
    var rating: Double? = null,

    var toc: String? = null,
    var cover: String? = null,
    var description: String? = null,
    var descriptionImage: String? = null,
    var categoryId: Category
) {
    @AssertTrue(message = "status 값은 0 또는 1이어야 합니다.")
    fun isValidStatus(): Boolean {
        return (status == null || status == 0 || status == 1)
    }
}
