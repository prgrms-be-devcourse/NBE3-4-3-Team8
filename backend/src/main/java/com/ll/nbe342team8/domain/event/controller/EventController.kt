package com.ll.nbe342team8.domain.event.controller

import jakarta.annotation.PostConstruct
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException

@RestController
@RequestMapping("/event")
class EventController {

    // 서버 실행 시 이미지를 저장해둘 리스트
    private var bannerImages: List<String> = emptyList()

    @PostConstruct
    @Throws(IOException::class)
    fun initBannerImages() {
        val location = "classpath:static/images/eventBanner/"
        val resolver = PathMatchingResourcePatternResolver()
        val resources: Array<Resource> = resolver.getResources("$location**")

        // Spring 실행 시 처음 한 번만 리스트에 이미지 저장
        bannerImages = resources.mapNotNull { resource ->
            resource.filename?.let { "/images/eventBanner/$it" }
        }
    }

    @GetMapping("/banners")
    fun getBannerImages(): List<String> {
        return bannerImages
    }
}
