package com.maxiamikel19.birthday_notifier_api.repository;

import com.maxiamikel19.birthday_notifier_api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID>, JpaSpecificationExecutor<Member> {
    boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m WHERE EXTRACT(MONTH FROM m.birthDate) = :month AND EXTRACT(DAY FROM m.birthDate) = :day AND m.active=true")
    List<Member> findBirthdayByMonthAndDay(@Param("month") int month, @Param("day") int day);

    List<Member> findByIdNotIn(List<UUID> memberIds);

    @Query("SELECT m FROM Member m WHERE EXTRACT(MONTH FROM m.birthDate) = :month AND m.active=true ORDER BY EXTRACT(DAY FROM m.birthDate) ASC")
    List<Member> findBirthdaysByMonth(@Param("month") int month);
}
