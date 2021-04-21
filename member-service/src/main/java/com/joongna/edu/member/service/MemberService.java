package com.joongna.edu.member.service;

import com.joongna.edu.member.MemberDto;
import com.joongna.edu.member.MemberDto.AddReq;
import com.joongna.edu.member.MemberDto.PageRes;
import com.joongna.edu.member.MemberDto.UpdateStatus;
import com.joongna.edu.exception.NotFoundUserIdException;
import com.joongna.edu.member.domain.Members;
import com.joongna.edu.member.domain.MembersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j

@Service
@Transactional(readOnly = true)
public class MemberService {

    @Value("${sqs.queueNames.hello}")
    private String sqsStatusQueue;

    private final QueueMessagingTemplate messagingTemplate;
    private final MembersRepository membersRepository;

    public MemberService(QueueMessagingTemplate messagingTemplate,
        MembersRepository membersRepository) {
        this.messagingTemplate = messagingTemplate;
        this.membersRepository = membersRepository;
    }


    @Transactional
    public Long addMembers(AddReq req) {

        Members members = membersRepository.findByUid(req.getId()).orElse(null);
        if (members != null) {
            throw new DuplicateKeyException("exist id > " + req.getId());
        }
        Members saved = membersRepository.save(req.of());
        return saved.getSeq();
    }

    public PageRes findAll(PageRequest page) {

        Page<Members> members = membersRepository.findAll(page);

        return MemberDto.PageRes.toEntity(members);

    }

    public MemberDto.Res findById(String id) {
        Members members = membersRepository.findByUid(id).orElse(null);
        if (members == null) {
            throw new NotFoundUserIdException("member id is not exist >" + id);
        }
        return members.ofResponse();
    }


    public MemberDto.Res findBySeq(Long seq) {
        Members members = membersRepository.findById(seq).orElse(null);
        if (members == null) {
            throw new NotFoundUserIdException("member sequence is not exist >" + seq);
        }
        return members.ofResponse();
    }

    @Transactional
    public Long updateStatus(UpdateStatus req) {
        Long seq = req.getSeq();
        Members members = membersRepository.findById(seq).orElse(null);
        if (members == null) {
            throw new NotFoundUserIdException("member sequence is not exist >" + seq);
        }
        members.update(req);
        Members save = membersRepository.save(members);
        return save.getSeq();
    }

    public void sendChangeMessage(UpdateStatus req) {
        messagingTemplate.convertAndSend(sqsStatusQueue, req);

    }
}
