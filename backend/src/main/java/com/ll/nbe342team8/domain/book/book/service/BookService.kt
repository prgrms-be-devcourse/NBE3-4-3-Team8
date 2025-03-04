package com.ll.nbe342team8.domain.book.book.service

import com.ll.nbe342team8.domain.book.book.entity.Book
import com.ll.nbe342team8.domain.book.book.repository.BookRepository
import com.ll.nbe342team8.domain.book.book.type.BookSortType
import com.ll.nbe342team8.domain.book.book.type.SearchType
import com.ll.nbe342team8.global.exceptions.ServiceException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val externalBookApiService: ExternalBookApiService,
    private val bookRepository: BookRepository
) {

    fun getAllBooks(page: Int, pageSize: Int, bookSortType: BookSortType): Page<Book> {
        val sorts = arrayListOf<Sort.Order>()
        sorts.add(bookSortType.order)

        // 출간일을 보조 정렬 기준으로 추가
        if (bookSortType != BookSortType.PUBLISHED_DATE) {
            val baseSort = BookSortType.PUBLISHED_DATE
            sorts.add(baseSort.order)
        }

        val pageable = PageRequest.of(page, pageSize, Sort.by(sorts))
        return bookRepository.findAll(pageable)
    }

    fun getBookById(id: Long): Book {
        // Todo: GlobalExceptionHandler 를 통해 처리하도록 수정
        return bookRepository.findById(id)
            .orElseThrow {
                ServiceException(
                    HttpStatus.NOT_FOUND.value(),
                    "id에 해당하는 책이 없습니다."
                )
            }
    }

    fun count(): Long {
        return bookRepository.count()
    }

    fun create(book: Book): Book {
        return bookRepository.save(book)
    }

    fun createReview(book: Book, rating: Double): Book {
        book.createReview(rating)
        return bookRepository.save(book)
    }

    fun deleteReview(book: Book, rating: Double): Book {
        book.deleteReview(rating)
        return bookRepository.save(book)
    }

    @Transactional(readOnly = true)
    fun searchBooks(
        page: Int,
        pageSize: Int,
        bookSortType: BookSortType,
        searchType: SearchType,
        keyword: String
    ): Page<Book> {
        // 판매량, 평점, 리뷰 정렬 시 보조 정렬 기준으로 출간일(pubDate) 적용
        val pageable: Pageable = if (
            bookSortType == BookSortType.SALES_POINT ||
            bookSortType == BookSortType.RATING ||
            bookSortType == BookSortType.REVIEW_COUNT
        ) {
            PageRequest.of(
                page,
                pageSize,
                Sort.by(
                    Sort.Order(bookSortType.order.direction, bookSortType.order.property),
                    Sort.Order(Sort.Direction.DESC, "pubDate")
                )
            )
        } else {
            PageRequest.of(page, pageSize, Sort.by(bookSortType.order))
        }

        return when (searchType) {
            SearchType.AUTHOR -> bookRepository.findBooksByAuthorContaining(keyword, pageable)
            SearchType.ISBN13 -> bookRepository.findBooksByIsbn13(keyword, pageable)
            SearchType.PUBLISHER -> bookRepository.findBooksByPublisherContaining(keyword, pageable)
            SearchType.TITLE -> bookRepository.findBooksByTitleContaining(keyword, pageable)
            else -> bookRepository.findBooksByTitleContaining(keyword, pageable)
        }
    }
}
