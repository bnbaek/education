package com.joongna.edu.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.joongna.edu.member.MemberPushDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

@Configuration
public class SseConfig {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {

        return Jackson2ObjectMapperBuilder.json()

            .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .modules(new JavaTimeModule())
            .build();
    }

    // autocanel 설정: true - subscribe가 하나도 없으면 processor 종료, false - subscribe가 없더라 processor 유지
    @Bean
    public EmitterProcessor<MemberPushDto.StatusRes> emitterProcessor() {
        return EmitterProcessor.create(false);
    }

    // OverFlow가 생길 경우, 과거의 데이터를 버리고 최신 데이터만 유지
    @Bean
    @Qualifier("eFluxSink")
    public FluxSink<MemberPushDto.StatusRes> emitterProcessorFluxSink(
        EmitterProcessor<MemberPushDto.StatusRes> emitterProcessor) {
        return emitterProcessor.sink(FluxSink.OverflowStrategy.LATEST);
    }

}
