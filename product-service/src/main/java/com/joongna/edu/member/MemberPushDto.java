package com.joongna.edu.member;

import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberPushDto {

    @Getter
    @NoArgsConstructor
    public static class StatusRes {

        private Long seq;
        private Status status;

        private Status beforeStatus;
        private LocalDateTime requestAt;

        public StatusRes(Long seq, Status status, Status beforeStatus, LocalDateTime requestAt) {
            this.seq = seq;
            this.status = status;
            this.beforeStatus = beforeStatus;
            if (requestAt == null) {
                this.requestAt = LocalDateTime.now();
                return ;

            }
            this.requestAt = requestAt;
        }
    }


    @Getter
    @AllArgsConstructor
    public enum Status {

        //@formatter:off
        WAITING(0, "가입대기")
        , USING(1, "사용")
        , SANCTIONS_FOR_USE(-9, "이용제재");
        //@formatter:on
        private int code;
        private String desc;

        public static Status ofCode(Integer code) {
            if (code == null) {
                return Status.WAITING;
            }
            return Arrays.stream(Status.values())
                .filter(v -> v.getCode() == code)
                .findAny()
                .orElse(Status.WAITING)
                ;
        }
    }

}
