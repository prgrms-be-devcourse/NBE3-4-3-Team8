package com.ll.nbe342team8.domain.book.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.nbe342team8.domain.book.book.dto.BookResponseDto;
import com.ll.nbe342team8.domain.book.book.dto.ExternalBookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalBookApiService {

    @Autowired  private WebClient webClient;
    @Autowired private ObjectMapper objectMapper;
    @Value("${aladin.ttbkey}")private String ttbkey;

//    public ExternalBookApiService(WebClient.Builder webClientBuilder,
//                                  @Value("${aladin.ttbkey}") String ttbkey) {
//        this.webClient = webClientBuilder.baseUrl("http://www.aladin.co.kr/ttb/api/").build();
//        this.ttbkey = ttbkey;
//    }

    public ExternalBookDto searchBook(String isbn13) {
        if (isbn13 == null || isbn13.isEmpty()) {
            throw new IllegalArgumentException("ISBN13 값은 필수입니다.");
        }

        String url = "https://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?"
                + "ttbkey=ttbkumjjick2245001"
                + "&itemIdType=ISBN13"
                + "&ItemId=" + isbn13
                + "&output=js"
                + "&Cover=Big";
        String response =  webClient.get()
                .uri(url) // 호출할 URL
                .retrieve() // 응답 데이터를 가져옴
                .bodyToMono(String.class) // 응답 데이터를 ExternalBookDto 로 변환
                .block(); // 비동기 호출을 동기적으로 처리
        try{

            return objectMapper.readValue(response,ExternalBookDto.class)   ;
        }catch (Exception e ){
            throw new RuntimeException("");
        }
    }
}
