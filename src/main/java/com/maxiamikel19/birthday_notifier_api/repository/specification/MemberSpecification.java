package com.maxiamikel19.birthday_notifier_api.repository.specification;

import com.maxiamikel19.birthday_notifier_api.dto.request.MemberFilter;
import com.maxiamikel19.birthday_notifier_api.entity.Member;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MemberSpecification {

    public static Specification<Member> withFilters(MemberFilter filter){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(filter.email() != null && !filter.email().isBlank()){
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")),
                                "%" + filter.email() + "%"
                        )
                );
            }

            if(filter.firstName() != null && !filter.firstName().isBlank()){
                predicates.add(
                  criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("firstName")),
                          "%" + filter.firstName().toLowerCase() + "%"
                  )
                );
            }

            if(filter.phoneNumber() != null && !filter.phoneNumber().isBlank()){
                predicates.add(
                  criteriaBuilder.like(
                          criteriaBuilder.lower(root.get("phoneNumber")),
                          "%" + filter.phoneNumber().toLowerCase() + "%"
                  )
                );
            }

            if(filter.gender() != null && !filter.gender().isBlank()){
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("gender"), filter.gender()
                        )
                );
            }

            if(filter.month() != null){
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.function(
                                        "date_part",
                                        Double.class,
                                        criteriaBuilder.literal("month"),
                                        root.get("birthDate")
                                ),
                                filter.month().doubleValue()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
