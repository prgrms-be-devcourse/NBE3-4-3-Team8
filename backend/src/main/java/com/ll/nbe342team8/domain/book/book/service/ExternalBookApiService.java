package com.ll.nbe342team8.domain.book.book.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ll.nbe342team8.domain.book.book.dto.ExternalBookDto;

@Service
public class ExternalBookApiService {

	private final WebClient webClient;
	private final String ttbkey;
	private final ObjectMapper objectMapper;

	public ExternalBookApiService(WebClient.Builder webClientBuilder, @Value("${aladin.ttbkey}") String ttbkey) {
		this.webClient = webClientBuilder.baseUrl("http://www.aladin.co.kr/ttb/api/").build();
		this.ttbkey = ttbkey;
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
	}

	public ExternalBookDto searchBookByIsbn13(String isbn13) {
		if (isbn13 == null || isbn13.isEmpty()) {
			throw new IllegalArgumentException("ISBN13 값이 없습니다.");
		}

		String url = String.format("ItemLookUp.aspx?ttbkey=%s&itemIdType=ISBN13&ItemId=%s&output=JS&Cover=Big",
				ttbkey, isbn13);

		try {
			// 응답을 String으로 받고 JSON 변환
			String jsonResponse = webClient.get()
					.uri(url)
					.retrieve()
					.bodyToMono(String.class) // 응답을 String으로 받음
					.block(); // 비동기 -> 동기 변환

			if (jsonResponse == null || jsonResponse.isEmpty()) {
				System.out.println("API 응답이 비어 있습니다.");
				return null;
			}

			// JSON을 객체로 변환
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			JsonNode itemNode = rootNode.path("item").get(0); // 첫 번째 도서 정보 추출

			if (itemNode.isMissingNode()) {
				System.out.println("API 응답에 도서 정보가 없습니다.");
				return null;
			}

			return objectMapper.treeToValue(itemNode, ExternalBookDto.class);

		} catch (Exception e) {
			System.out.println("API 요청 또는 응답 변환 오류: " + e.getMessage());
			return null;
		}
	}
}
