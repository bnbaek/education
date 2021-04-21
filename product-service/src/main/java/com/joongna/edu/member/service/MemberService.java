package com.joongna.edu.member.service;

import com.joongna.edu.member.MemberPushDto;
import com.joongna.edu.member.MemberPushDto.StatusRes;
import java.time.Duration;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
@Service
public class MemberService {

    private final EmitterProcessor<MemberPushDto.StatusRes> emitterProcessor;
    private final FluxSink<StatusRes> emitterProcessorFluxSink;

    public MemberService(
        EmitterProcessor<StatusRes> emitterProcessor,
        FluxSink<StatusRes> emitterProcessorFluxSink) {
        this.emitterProcessor = emitterProcessor;
        this.emitterProcessorFluxSink = emitterProcessorFluxSink;
    }

    public Flux<ServerSentEvent<String>> subscriptionStatus(Long seq) {
        if (seq == null || seq < 1) {
            return Flux.error(new IllegalStateException("invalid seq"));
        }
        return emitterProcessor
            .publishOn(Schedulers.elastic())
            .filter(model -> seq.equals(model.getSeq()))
            .map(model -> ServerSentEvent.builder(model.getStatus().name()).build())
            .doOnCancel(() -> System.out.println("Client is disconnected"+ seq));
    }

    public Mono<Void> produceData(StatusRes req) {
        emitterProcessorFluxSink.next(req);
        return Mono.empty();
    }


    private  Flux<ServerSentEvent<String>> ping() {
        return Flux.interval(Duration.ofMillis(500))
            .map(i -> ServerSentEvent.<String>builder().build());
    }
}
