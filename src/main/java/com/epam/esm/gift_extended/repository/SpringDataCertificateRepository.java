package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;

@Repository
public interface SpringDataCertificateRepository extends PagingAndSortingRepository<Certificate, Integer> {

    Optional<Certificate> findByName(String name);

    List<Certificate> findDistinctByDescriptionContainingAndNameContaining(String pattern, String pattern1,
            Pageable pageable);

    @Query("SELECT DISTINCT cert " + "FROM Certificate cert " + "join cert.tags as tag " + "WHERE tag in :tags "
                   + "group by cert having count(tag)=:tagSize ")
    List<Certificate> findByContainsAllTagNames(List<Tag> tags, Pageable pageable);

    @Query("SELECT C FROM Order O join O.customer cust JOIN O.certificates C join C.tags as T WHERE T=:tag and cust=:user ")
    List<Certificate> findCertificateByHolderAndTag(User user, Tag tag, Pageable pageable);

    @Query("SELECT cert FROM Order O join O.customer customer join O.certificates cert WHERE customer.userId=:userId ")
    List<Certificate> findUserCertificates(int userId, Pageable pageable);

    @Query("SELECT cert FROM Order  O join O.customer customer join O.certificates cert WHERE customer.userId=:userId ")
    List<Certificate> findUserCertificates(int userId);
}
