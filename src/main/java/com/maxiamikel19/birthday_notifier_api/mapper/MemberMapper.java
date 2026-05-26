package com.maxiamikel19.birthday_notifier_api.mapper;

import com.maxiamikel19.birthday_notifier_api.dto.request.MemberCreateRequest;
import com.maxiamikel19.birthday_notifier_api.dto.response.MemberResponse;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.entity.enums.MemberRole;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MemberMapper {

    public MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getFullName(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getAffiliationNumber(),
                member.getGender().name(),
                member.getMemberRole().name(),
                member.getActive() == true ? "activated" : "deactivated",
                member.getAffiliationDate(),
                member.getAge(),
                member.getAffiliationYears(),
                member.getCreatedAt(),
                member.getUpdatedAt());

    }

    public Member toEntity(MemberCreateRequest request) {
        return Member.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .gender(request.gender())
                .memberRole(MemberRole.MEMBER)
                .active(true)
                .affiliationNumber(UUID.randomUUID().toString().substring(0, 14))
                .birthDate(request.birthDate())
                .affiliationDate(request.affiliationDate())
                .build();
    }
}
