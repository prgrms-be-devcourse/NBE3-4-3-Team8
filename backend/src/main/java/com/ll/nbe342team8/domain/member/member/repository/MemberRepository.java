package com.ll.nbe342team8.domain.member.member.repository;

import com.ll.nbe342team8.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
<<<<<<< HEAD
    Optional<Member> findByEmail(String email);
=======
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
>>>>>>> dev
}
=======
    Optional<Member> findByName(String name);
}
>>>>>>> origin/feature-order-search
