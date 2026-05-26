package com.maxiamikel19.birthday_notifier_api.entity;

import com.maxiamikel19.birthday_notifier_api.entity.enums.Gender;
import com.maxiamikel19.birthday_notifier_api.entity.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "members", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_phonne_number", columnList = "phone_number"),
        @Index(name = "idx_birth_date", columnList = "birth_date"),
        @Index(name = "idx_affiliation_date", columnList = "affiliation_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Member extends BaseEntity{

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "affiliation_number", unique = true, nullable = false, length = 15)
    private String affiliationNumber;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false, length = 30)
    private MemberRole memberRole;

    @Column(name = "affiliation_date", nullable = false, updatable = false)
    private LocalDate affiliationDate;

    @Column(nullable = false)
    private Boolean active = true;

    public int getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAffiliationYears() {
        return Period.between(this.affiliationDate, LocalDate.now()).getYears();
    }

    @PrePersist
    @PreUpdate
    public void normalizeFields() {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim().toLowerCase();
        this.phoneNumber = phoneNumber.trim();
    }
}
