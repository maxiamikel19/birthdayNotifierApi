package com.maxiamikel19.birthday_notifier_api.controller;

import com.maxiamikel19.birthday_notifier_api.dto.response.MemberResponse;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.mapper.MemberMapper;
import com.maxiamikel19.birthday_notifier_api.service.IBirthdayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/birthdays")
@RequiredArgsConstructor
public class BirthdayController {

    private final IBirthdayService birthdayService;
    private final MemberMapper memberMapper;

    @GetMapping("/today")
    public ResponseEntity<List<MemberResponse>> getCurrentBirthdays(){
        return ResponseEntity.ok().body(
                mapToResponse(
                        birthdayService.getCurrentBirthdays()
                )
        );
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<MemberResponse>> getUpcomingBirthdays(){
        return ResponseEntity.ok().body(
                mapToResponse(
                        birthdayService.getUpcomingBirthdays()
                )
        );
    }

    @GetMapping("/this-month")
    public ResponseEntity<List<MemberResponse>> getAllMonthBirthdays(){
        return ResponseEntity.ok().body(
                mapToResponse(
                        birthdayService.getBirthdaysThisMonth()
                )
        );
    }

    private List<MemberResponse> mapToResponse(List<Member> members) {
        return members.stream()
                .map(memberMapper::toResponse)
                .toList();
    }
}
