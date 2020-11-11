package com.epam.esm.gift_extended.repository.forbidentouse;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;

public interface CertificateRepositoryWith extends PagingAndSortingRepository<Certificate,Integer> {

    @Query(value = "SELECT c FROM Certificate c WHERE c.holder.userId=?1")
    List<Certificate> findUserCertificates(Integer userId);

   Iterable<Certificate> findDistinctByTags(Tag tag);


   @Query(value = "SELECT cert "
           + "FROM Certificate cert "
           + "join cert.tags as tag "
           + "WHERE tag in :tags "
           + "group by cert having count(tag)=(SELECT count(tt) from Tag as tt WHERE tt in :tags)")
   Iterable<Certificate> findByContainsAllTagNames(List<Tag> tags);


    List<Certificate> findByName(String name);

    List<Certificate> findCertificateByHolderAndTags(User holder,Tag tag);

    @Query(value = "SELECT c FROM Certificate c "
            + "WHERE c.name like CONCAT('%',:name,'%') or c.description like CONCAT('%',:description,'%')")
    List<Certificate> findDistinctByDescriptionContainingAndNameContaining(String name,String description);
}
