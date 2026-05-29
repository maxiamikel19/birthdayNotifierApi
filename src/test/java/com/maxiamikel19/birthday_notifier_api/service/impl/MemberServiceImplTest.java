package com.maxiamikel19.birthday_notifier_api.service.impl;

import com.maxiamikel19.birthday_notifier_api.dto.request.MemberCreateRequest;
import com.maxiamikel19.birthday_notifier_api.dto.request.MemberFilter;
import com.maxiamikel19.birthday_notifier_api.dto.request.UpdateMemberRequest;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;
import com.maxiamikel19.birthday_notifier_api.entity.enums.MemberRole;
import com.maxiamikel19.birthday_notifier_api.exception.InputValidationException;
import com.maxiamikel19.birthday_notifier_api.exception.ResourceDuplicatedException;
import com.maxiamikel19.birthday_notifier_api.exception.ResourceNotFoundException;
import com.maxiamikel19.birthday_notifier_api.mapper.MemberMapper;
import com.maxiamikel19.birthday_notifier_api.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    private MemberCreateRequest createRequest;
    private UpdateMemberRequest updateRequest;
    private Member member;
    private UUID memberId;

    @BeforeEach
    void setUp() {
        memberId = UUID.fromString("11111111-2222-3333-4444-555555555555");
        member = Member.builder()
                .id(memberId)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("+123456789")
                .gender(Gender.FEMALE)
                .memberRole(MemberRole.MEMBER)
                .birthDate(LocalDate.of(1990, 5, 10))
                .affiliationDate(LocalDate.of(2020, 5, 10))
                .active(true)
                .affiliationNumber("initial-0000")
                .build();

        createRequest = new MemberCreateRequest(
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "+123456789",
                LocalDate.of(1990, 5, 10),
                Gender.FEMALE,
                LocalDate.of(2020, 5, 10));

        updateRequest = new UpdateMemberRequest(
                "Janet",
                "Smith",
                "janet.smith@example.com",
                "+987654321",
                LocalDate.of(2021, 6, 1),
                MemberRole.MEMBER,
                false);
    }

    @Test
    void create_shouldSaveMemberAndGenerateAffiliationNumber() {
        when(memberRepository.existsByEmail(createRequest.email())).thenReturn(false);
        when(memberMapper.toEntity(createRequest)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);

        Member saved = memberService.create(createRequest);

        assertThat(saved).isSameAs(member);
        assertThat(saved.getAffiliationNumber()).isEqualTo("19900510-1111");
        verify(memberRepository).existsByEmail(createRequest.email());
        verify(memberMapper).toEntity(createRequest);
        verify(memberRepository).save(member);
        verifyNoMoreInteractions(memberRepository, memberMapper);
    }

    @Test
    void create_shouldThrowWhenEmailAlreadyExists() {
        when(memberRepository.existsByEmail(createRequest.email())).thenReturn(true);

        assertThrows(ResourceDuplicatedException.class, () -> memberService.create(createRequest));

        verify(memberRepository).existsByEmail(createRequest.email());
        verifyNoMoreInteractions(memberRepository, memberMapper);
    }

    @Test
    void create_shouldThrowWhenPhoneNumberDoesNotStartWithPlus() {
        MemberCreateRequest invalidPhoneRequest = new MemberCreateRequest(
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "123456789",
                LocalDate.of(1990, 5, 10),
                Gender.FEMALE,
                LocalDate.of(2020, 5, 10));

        assertThrows(InputValidationException.class, () -> memberService.create(invalidPhoneRequest));
    }

    @Test
    void create_shouldThrowWhenAffiliationDateBeforeBirthDate() {
        MemberCreateRequest invalidAffiliationRequest = new MemberCreateRequest(
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "+123456789",
                LocalDate.of(1990, 5, 10),
                Gender.FEMALE,
                LocalDate.of(1989, 12, 31));

        assertThrows(InputValidationException.class, () -> memberService.create(invalidAffiliationRequest));

    }

    @Test
    void findById_shouldReturnMemberWhenFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Member found = memberService.findById(memberId);

        assertThat(found).isSameAs(member);
        verify(memberRepository).findById(memberId);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.findById(memberId));

        verify(memberRepository).findById(memberId);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void searchMembers_shouldThrowWhenPageSizeGreaterThanFifty() {
        MemberFilter filter = new MemberFilter(null, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 51);

        assertThrows(InputValidationException.class, () -> memberService.searchMembers(filter, pageable));
    }

    @Test
    void searchMembers_shouldThrowWhenInvalidMonth() {
        MemberFilter filter = new MemberFilter(null, null, null, null, 13);
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(InputValidationException.class, () -> memberService.searchMembers(filter, pageable));
    }

    @Test
    void searchMembers_shouldReturnPageFromRepository() {
        MemberFilter filter = new MemberFilter("jane.doe@example.com", null, null, null, 5);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Member> expectedPage = new PageImpl<>(List.of(member), pageable, 1);

        when(memberRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(expectedPage);

        Page<Member> result = memberService.searchMembers(filter, pageable);

        assertThat(result).isSameAs(expectedPage);
        verify(memberRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void update_shouldSaveUpdatedMemberWhenEmailChanges() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(memberRepository.existsByEmail(updateRequest.email())).thenReturn(false);
        when(memberRepository.save(member)).thenReturn(member);

        Member updated = memberService.update(memberId, updateRequest);

        assertThat(updated).isSameAs(member);
        assertThat(updated.getFirstName()).isEqualTo("Janet");
        assertThat(updated.getLastName()).isEqualTo("Smith");
        assertThat(updated.getEmail()).isEqualTo("janet.smith@example.com");
        assertThat(updated.getPhoneNumber()).isEqualTo("+987654321");
        assertThat(updated.getAffiliationDate()).isEqualTo(LocalDate.of(2021, 6, 1));
        assertThat(updated.getMemberRole()).isEqualTo(MemberRole.MEMBER);
        assertThat(updated.getActive()).isFalse();

        verify(memberRepository).findById(memberId);
        verify(memberRepository).existsByEmail(updateRequest.email());
        verify(memberRepository).save(member);
    }

    @Test
    void update_shouldThrowWhenMemberNotFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.update(memberId, updateRequest));

        verify(memberRepository).findById(memberId);
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    void delete_shouldRemoveMemberWhenFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        doNothing().when(memberRepository).delete(member);

        memberService.delete(memberId);

        verify(memberRepository).findById(memberId);
        verify(memberRepository).delete(member);
    }

    @Test
    void delete_shouldThrowWhenMemberNotFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.delete(memberId));

        verify(memberRepository).findById(memberId);
        verifyNoMoreInteractions(memberRepository);
    }
}
