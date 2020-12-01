package com.epam.esm.gift_extended.repository;

import java.util.List;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

public interface CertificateRepository extends GiftRepository<Certificate> {

    List<Certificate> findUserCertificates(Integer userId);

    List<Certificate> findByContainsAllTagNames(List<Tag> tags);

    List<Certificate> findCertificateByHolderAndTag(User holder, Tag tag);

    List<Certificate> findDistinctByDescriptionContainingAndNameContaining(String name, String description);

    List<Certificate> findUserCertificates(int userId, PageSortInfo pageable);

    List<Certificate> findCertificateByHolderAndTag(User user, Tag tag, PageSortInfo pageable);

    List<Certificate> findByContainsAllTagNames(List<Tag> tags, PageSortInfo pageable);

    List<Certificate> findDistinctByDescriptionContainingAndNameContaining(String pattern, String pattern1, PageSortInfo pageable);
}
