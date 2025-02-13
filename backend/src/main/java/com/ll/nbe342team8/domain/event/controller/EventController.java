//package com.ll.nbe342team8.domain.event.controller;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/event")
//public class EventController {
//
//    //서버 실행 시 이미지를 저장해둘 리스트
//    private List<String> bannerImages;
//
//    @PostConstruct
//    public void initBannerImages() throws IOException {
//        String location = "classpath:static/images/eventBanner/";
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        Resource[] resources = resolver.getResources(location + "**");
//
//        // spring 실행 시 처음 한번만 리스트에 이미지 저장
//        // 프론트에서 요청 시 리스트 반환
//        bannerImages = Arrays.stream(resources)
//                .map(resource -> "/images/eventBanner/" + resource.getFilename())
//                .collect(Collectors.toList());
//    }
//
//    @GetMapping("/banners")
//    public List<String> getBannerImages() {
//        return bannerImages;
//    }
//
//}
//
