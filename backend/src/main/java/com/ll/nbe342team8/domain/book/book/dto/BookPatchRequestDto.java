package com.ll.nbe342team8.domain.book.book.dto;

import com.ll.nbe342team8.domain.book.category.entity.Category;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookPatchRequestDto {
    private String title;
    private String author;

    @Pattern(regexp = "^[0-9]{10}$", message = "ISBN은 10자리 숫자여야 합니다.")
    private String isbn;   // 10자리 숫자만 허용

    @Pattern(regexp = "^[0-9]{13}$", message = "ISBN13은 13자리 숫자여야 합니다.")
    private String isbn13; // 13자리 숫자만 허용

    private LocalDate pubDate;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer priceStandard;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer priceSales;

    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    @AssertTrue(message = "status 값은 0 또는 1이어야 합니다.")
    public boolean isValidStatus() {
        return status == null || status == 0 || status == 1;
    }

    private Integer status;

    @DecimalMin(value = "0.0", message = "평점은 0 이상이어야 합니다.")
    private Double rating;

    private String toc;
    private String cover;
    private String description;
    private String descriptionImage;
    private Category categoryId;

}
