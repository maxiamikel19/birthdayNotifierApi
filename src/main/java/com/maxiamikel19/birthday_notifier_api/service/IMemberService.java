package com.maxiamikel19.birthday_notifier_api.service;

import com.maxiamikel19.birthday_notifier_api.dto.request.MemberCreateRequest;
import com.maxiamikel19.birthday_notifier_api.dto.request.UpdateMemberRequest;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IMemberService {

    Member create(MemberCreateRequest request);

    Member findById(UUID id);

    Page<Member> searchMembers(Pageable pageable);

    Member update(UUID id, UpdateMemberRequest request);

    void delete(UUID id);
}
