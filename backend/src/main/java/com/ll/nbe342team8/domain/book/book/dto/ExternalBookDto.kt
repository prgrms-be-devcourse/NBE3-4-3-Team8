package com.ll.nbe342team8.domain.book.book.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDate

// 외부 API에서 받아온 도서 정보를 담는 DTO
@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class ExternalBookDto(
	val title: String?,  // 제목
	val author: String?,  // 저자
	val publisher: String?,  // 출판사
	val pubDate: LocalDate?,  // 출판일
	val isbn: String?,  // ISBN
	val isbn13: String?,  // ISBN13
	val priceStandard: Int?,  // 정가
	val priceSales: Int?,  // 판매가
	val toc: String?,  // 목차
	val cover: String?,  // 커버 이미지 URL
	val description: String?,  // 상세 설명
	val descriptionImage: String?,  // 상세 설명 이미지 URL
	val categoryId: Int // 알라딘 API에서 받아온 카테고리 ID (Category Pk 아님)
)
