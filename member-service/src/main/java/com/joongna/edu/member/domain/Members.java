package com.joongna.edu.member.domain;

import com.joongna.edu.member.MemberDto;
import com.joongna.edu.member.MemberDto.Res;
import com.joongna.edu.member.MemberDto.UpdateStatus;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "members")
@Entity

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long seq;

    @Column(name = "id", nullable = false)
    private String uid;

    @Column(name = "name", nullable = false)
    private String name;

    private MemberDto.Status status;

    @Column(name = "password")
    private String password;

    @Column(name = "age")
    private Integer age;

    private LocalDateTime createdAt;


    public Members(String id, String name, MemberDto.Status status) {
        this.uid = id;
        this.name = name;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Members(String id, String name, String password, MemberDto.Status status) {
        this.uid = id;
        this.name = name;
        this.password = password;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Res ofResponse() {
        return new Res(this.seq, this.uid, this.name, this.status, this.createdAt);
    }

    public void update(UpdateStatus req) {
        if (this.status == req.getStatus()) {
            throw new IllegalArgumentException("user status");
        }
        req.setBeforeStatus(this.status);
        this.status = req.getStatus();
    }
}
