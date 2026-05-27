package com.maxiamikel19.birthday_notifier_api.service.impl;

import com.maxiamikel19.birthday_notifier_api.dto.request.MemberCreateRequest;
import com.maxiamikel19.birthday_notifier_api.dto.request.MemberFilter;
import com.maxiamikel19.birthday_notifier_api.dto.request.UpdateMemberRequest;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.exception.InputValidationException;
import com.maxiamikel19.birthday_notifier_api.exception.ResourceDuplicatedException;
import com.maxiamikel19.birthday_notifier_api.exception.ResourceNotFoundException;
import com.maxiamikel19.birthday_notifier_api.mapper.MemberMapper;
import com.maxiamikel19.birthday_notifier_api.repository.MemberRepository;
import com.maxiamikel19.birthday_notifier_api.repository.specification.MemberSpecification;
import com.maxiamikel19.birthday_notifier_api.service.IMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements IMemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;


    @Override
    @Transactional
    public Member create(MemberCreateRequest request) {

        validateEmailAvailability(request.email());

        validatePhoneNumber(request.phoneNumber());

        validateAffiliationDate(request.birthDate(), request.affiliationDate());

        Member member = memberMapper.toEntity(request);

         member = memberRepository.save(member);

        member.setAffiliationNumber(generateCode(member));

        log.info("Member created successfully with email: {}", member.getEmail());

        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public Member findById(UUID id) {
        return getMemberOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Member> searchMembers(MemberFilter filter, Pageable pageable) {
        if (pageable.getPageSize() > 50) {
            throw new InputValidationException("Maximum page size allowed is 50");
        }

        if(filter.month() != null && (filter.month() < 1 || filter.month() > 12)){
            throw new InputValidationException("Month value must be between 1 and 12");
        }

        Specification<Member> specification = MemberSpecification.withFilters(filter);
        return memberRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Member update(UUID id, UpdateMemberRequest request) {
        Member member = getMemberOrThrow(id);
        if (!member.getEmail().equals(request.email())) {
            validateEmailAvailability(request.email());
        }
        updateEntityFromRequest(request, member);
        log.info("Updating member: {}", id);
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Member member = getMemberOrThrow(id);
        log.info("Deleting  member: {}", id);
        memberRepository.delete(member);
    }

    private void validateEmailAvailability(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new ResourceDuplicatedException("Email already registered");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.startsWith("+")) {
            throw new InputValidationException("Phone number must start with '+'");
        }
    }

    private void validateAffiliationDate(LocalDate birthDate, LocalDate affiliationDate) {
        if (affiliationDate.isBefore(birthDate)) {
            throw new InputValidationException("Affiliation date cannot be before birth date");
        }
    }

    private String generateCode(Member member) {
        String birthDatePart = member.getBirthDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String memberPrefix = member.getId()
                .toString()
                .replace("-", "")
                .substring(0, 4)
                .toUpperCase();
        return String.format("%s-%s", birthDatePart, memberPrefix);
    }

    private Member getMemberOrThrow(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    private void updateEntityFromRequest(UpdateMemberRequest request, Member member){
        if (request.firstName() != null) {
            member.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            member.setLastName(request.lastName());
        }
        if (request.email() != null) {
            member.setEmail(request.email());
        }
        if (request.phoneNumber() != null) {
            member.setPhoneNumber(request.phoneNumber());
        }
        if (request.affiliationDate() != null) {
            member.setAffiliationDate(request.affiliationDate());
        }
        if (request.memberRole() != null) {
            member.setMemberRole(request.memberRole());
        }
        if (request.status() != null) {
            member.setActive(request.status());
        }
    }

}
