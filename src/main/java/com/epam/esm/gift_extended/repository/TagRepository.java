package com.epam.esm.gift_extended.repository;

import java.util.List;
import java.util.Optional;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

public interface TagRepository extends GiftRepository<Tag>{

    Optional<Tag> findByName(String name);

    Optional<Tag> findMostPopularTagFromRichestUserBySumOfCertificatePrice();

    List<Tag> findByCert(int certId, PageSortInfo pageable);
}
