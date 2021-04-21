package com.joongna.edu.member.api;

import com.joongna.edu.member.MemberDto.AddReq;
import com.joongna.edu.member.MemberDto.PageRes;
import com.joongna.edu.member.MemberDto.Res;
import com.joongna.edu.member.MemberDto.UpdateStatus;
import com.joongna.edu.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j

@RequestMapping(value = "/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping
    public ResponseEntity<Long> welcome(@RequestBody AddReq req) {
        req.validate();

        Long seq = memberService.addMembers(req);
        return ResponseEntity.ok(seq);
    }

    @GetMapping
    public ResponseEntity<PageRes> getMembers() {
        //추후 확장
        PageRequest page = PageRequest.of(0, 10, Sort.by(Order.desc("createdAt")));

        return ResponseEntity.ok(memberService.findAll(page));
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<Res> getMember(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("invalid id >" + id);
        }

        Res member = memberService.findById(id);

        return ResponseEntity.ok(member);
    }

    @GetMapping(value = "/{seq}")
    public ResponseEntity<Res> getMember(@PathVariable("seq") Long seq) {
        if (seq == null || seq < 1L) {
            throw new IllegalArgumentException("invalid sequence >" + seq);
        }

        Res member = memberService.findBySeq(seq);

        return ResponseEntity.ok(member);
    }

    @PutMapping("/status")
    public ResponseEntity<Long> updateStatus(@RequestBody UpdateStatus req) {
        req.validate();
        Long seq = memberService.updateStatus(req);
        log.info("[UPDATE] member changed : {} -> {}",req.getBeforeStatus(),req.getStatus());
        memberService.sendChangeMessage(req);
        return ResponseEntity.ok(seq);
    }



}
