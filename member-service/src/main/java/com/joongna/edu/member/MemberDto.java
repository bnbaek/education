package com.joongna.edu.member;

import com.joongna.edu.member.domain.Members;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

public class MemberDto {

    @Getter
    @NoArgsConstructor
    public static class AddReq {

        private String id;
        private String name;

        private String password;
        private MemberDto.Status status;


        public void validate() {
            if (StringUtils.isEmpty(id)) {
                throw new IllegalArgumentException("not exist id > " + id);
            }

            if (StringUtils.isEmpty(name)) {
                throw new IllegalArgumentException("not exist id > " + name);
            }
            if (status == null) {
                this.status = Status.WAITING;
            }
        }

        public Members of() {
            return
                new Members(this.id, this.name, this.password, this.status);

        }
    }

    @Getter
    public static class Res {

        private Long seq;
        private String id;

        private String name;

        private MemberDto.Status status;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime requestAt;

        public Res(Long seq, String id, String name, MemberDto.Status status, LocalDateTime requestAt) {
            this.seq = seq;
            this.id = id;
            this.name = name;
            this.status = status;
            this.requestAt = requestAt;
        }
    }

    @Getter
    public static class PageRes {

        private int totalPages;
        private List<Res> members;
        private int requestPage;

        public PageRes(int totalPages, List<Res> members, int requestPage) {
            this.totalPages = totalPages;
            this.members = members;
            this.requestPage = requestPage;
        }

        public static PageRes toEntity(Page<Members> datas) {
            if (datas.isEmpty()) {
                return new PageRes(datas.getTotalPages(), null, datas.getNumber());
            }

            List<Res> res = datas.toList().stream().map(e -> e.ofResponse())
                .collect(Collectors.toList());
            return new PageRes(datas.getTotalPages(), res, datas.getNumber());
        }
    }

    @Getter
    public static class UpdateStatus {

        private Long seq;
        private Status status;

        private Status beforeStatus;
        private LocalDateTime requestAt;

        public void validate() {
            if (seq == null || seq < 1) {
                throw new IllegalArgumentException("invalid seq >" + seq);
            }
            if (status == null) {
                throw new IllegalArgumentException("invalid status >" + status);
            }
            this.requestAt = LocalDateTime.now();
        }

        public void setBeforeStatus(Status beforeStatus) {
            this.beforeStatus = beforeStatus;
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
