package com.ll.nbe342team8.Book.Book;

import com.ll.nbe342team8.domain.book.book.entity.Book;
import com.ll.nbe342team8.domain.book.book.service.BookService;
import com.ll.nbe342team8.domain.book.book.type.BookSortType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("전체 도서 조회 테스트")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetAllBooks() throws Exception {
        // GET /books?page=0&pageSize=10&bookSortType=PUBLISHED_DATE 로 호출
        ResultActions resultActions = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("bookSortType", BookSortType.PUBLISHED_DATE.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // 서비스 계층에서 데이터를 조회하여 비교
        Page<Book> bookPage = bookService.getAllBooks(0, 10, BookSortType.PUBLISHED_DATE);

        resultActions
                .andExpect(status().isOk())
                // 반환 JSON의 content 필드가 배열인지 확인
                .andExpect(jsonPath("$.content").isArray())
                // 총 아이템 수가 일치하는지 검증
                .andExpect(jsonPath("$.totalElements").value(bookPage.getTotalElements()));
    }

    @Test
    @DisplayName("특정 도서 조회 테스트")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetBookById() throws Exception {
        // 테스트를 위해 DB에 존재하는 도서의 id를 조회
        Book book = bookService.getAllBooks(0, 10, BookSortType.PUBLISHED_DATE)
                .getContent().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("테스트용 도서를 찾을 수 없습니다."));
        Long bookId = book.getId();

        ResultActions resultActions = mockMvc.perform(get("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()));
    }

    @Test
    @DisplayName("도서 검색 테스트 - 제목 검색")
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testSearchBooks() throws Exception {
        // 검색 키워드를 "제목"으로 변경하여 초기 데이터와 일치하도록 함
        String keyword = "제목";

        ResultActions resultActions = mockMvc.perform(get("/books/search")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("bookSortType", BookSortType.PUBLISHED_DATE.name())
                        .param("searchType", "TITLE")
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                // 결과 항목의 제목이 검색 키워드를 포함하는지 검증
                .andExpect(jsonPath("$.content[0].title", Matchers.containsString(keyword)));
    }
}
