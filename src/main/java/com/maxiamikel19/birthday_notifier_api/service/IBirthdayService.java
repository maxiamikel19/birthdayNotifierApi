package com.maxiamikel19.birthday_notifier_api.service;

import com.maxiamikel19.birthday_notifier_api.entity.Member;

import java.util.List;

public interface IBirthdayService {

    List<Member> getCurrentBirthdays();

    List<Member> getUpcomingBirthdays();

    List<Member> getBirthdaysThisMonth();
}
