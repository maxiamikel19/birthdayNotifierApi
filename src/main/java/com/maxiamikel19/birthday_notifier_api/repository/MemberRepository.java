package com.maxiamikel19.birthday_notifier_api.repository;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    boolean existsByEmail(String email);
}
