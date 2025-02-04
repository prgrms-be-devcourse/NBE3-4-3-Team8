package com.ll.nbe342team8.domain.event.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/event")
public class EventController {

    @GetMapping("/banners")
    public List<String> getBannerImages() throws IOException {
        String location = "classpath:static/images/eventBanner/";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(location + "**");

        return Arrays.stream(resources)
                .map(resource -> "/images/eventBanner/" + resource.getFilename())
                .collect(Collectors.toList());
    }

}

