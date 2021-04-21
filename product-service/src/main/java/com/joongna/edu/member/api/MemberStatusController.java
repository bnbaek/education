package com.joongna.edu.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joongna.edu.member.MemberPushDto.Status;
import com.joongna.edu.member.MemberPushDto.StatusRes;
import com.joongna.edu.member.service.MemberService;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor

@RestController
public class MemberStatusController {


    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "/api/members/subscription/{seq}", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public ResponseEntity<Flux<ServerSentEvent<String>>> getAccountInfoSubscription(
        @PathVariable(value = "seq") Long memberSeq) {
        return ResponseEntity.ok(memberService.subscriptionStatus(memberSeq));
    }

    @PostMapping(value="/api/members")
    public Mono<Void> produceData() {
        StatusRes req = new StatusRes(1L,Status.USING, Status.USING, LocalDateTime.now());
        return memberService.produceData(req);
    }


    @SqsListener(value = "${sqs.queueNames.hello}")
    public void receive(String message, @Header("SenderId") String senderId) throws IOException {
        log.info("senderId: {}, message: {}", senderId, message);
        StatusRes statusRes = objectMapper.readValue(message, StatusRes.class);
        memberService.produceData(statusRes);
    }



}
