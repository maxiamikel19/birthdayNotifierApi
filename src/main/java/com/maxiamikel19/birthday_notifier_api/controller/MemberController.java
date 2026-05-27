package com.maxiamikel19.birthday_notifier_api.controller;

import com.maxiamikel19.birthday_notifier_api.dto.request.MemberCreateRequest;
import com.maxiamikel19.birthday_notifier_api.dto.request.UpdateMemberRequest;
import com.maxiamikel19.birthday_notifier_api.dto.response.MemberResponse;
import com.maxiamikel19.birthday_notifier_api.dto.response.PageResponse;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.mapper.MemberMapper;
import com.maxiamikel19.birthday_notifier_api.service.IMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final IMemberService memberService;
    private final MemberMapper memberMapper;

    @PostMapping
    public ResponseEntity<MemberResponse> create(
            @Valid @RequestBody MemberCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberMapper.toResponse(memberService.create(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(memberMapper.toResponse(memberService.findById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<MemberResponse>> searchMembers(
            @PageableDefault(size = 2, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        Page<Member> members = memberService.searchMembers(pageable);
        List<MemberResponse> memberList = members
                .stream()
                .map(memberMapper::toResponse).toList();
        PageResponse<MemberResponse> response = new PageResponse<MemberResponse>(
                memberList,
                members.getNumber(),
                members.getSize(),
                members.getTotalElements(),
                members.getTotalPages(),
                members.isLast());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        return ResponseEntity.ok(memberMapper.toResponse(memberService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
