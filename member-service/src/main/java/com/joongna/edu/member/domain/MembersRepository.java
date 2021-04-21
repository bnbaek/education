package com.joongna.edu.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembersRepository extends JpaRepository<Members, Long> {

    Optional<Members> findByUid(String seq);
}
