package com.maxiamikel19.birthday_notifier_api.service.impl;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import com.maxiamikel19.birthday_notifier_api.repository.MemberRepository;
import com.maxiamikel19.birthday_notifier_api.service.IBirthdayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BirthdayServiceImpl implements IBirthdayService {

    private  final MemberRepository memberRepository;

    @Value("${api.birthday.interval-days}")
    private int intervalDays;

    @Override
    public List<Member> getCurrentBirthdays() {
        LocalDate targetDate = LocalDate.now();
        return getBirthdaysByBirthDate(targetDate);
    }

    @Override
    public List<Member> getUpcomingBirthdays() {
        LocalDate targetDate = LocalDate.now().plusDays(intervalDays);
        return getBirthdaysByBirthDate(targetDate);
    }

    @Override
    public List<Member> getBirthdaysThisMonth() {
        LocalDate targetDate = LocalDate.now();
        return memberRepository.findBirthdaysByMonth(targetDate.getMonthValue());
    }

    private List<Member> getBirthdaysByBirthDate(LocalDate targetDate){
        int targetMonth = targetDate.getMonthValue();
        int targetDay = targetDate.getDayOfMonth();
        return memberRepository.findBirthdayByMonthAndDay(targetMonth, targetDay);
    }
}
