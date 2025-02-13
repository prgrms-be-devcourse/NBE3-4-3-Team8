package com.ll.nbe342team8.global.initData;

import com.ll.nbe342team8.domain.member.member.service.MemberService;
import com.ll.nbe342team8.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class DevInitData {

    @Autowired
    @Lazy
    private DevInitData self;

    @Bean
    public ApplicationRunner devInitDataApplicationRunner() {
        return args -> {
            Ut.file.downloadByHttp("http://localhost:8080/v3/api-docs", ".");

            String cmd = "yes | npx --package typescript --package openapi-typescript --package punycode openapi-typescript api-docs.json -o ./frontend/app/backend/api/schema.d.ts";
            Ut.cmd.runAsync(cmd);
        };
    }
}
