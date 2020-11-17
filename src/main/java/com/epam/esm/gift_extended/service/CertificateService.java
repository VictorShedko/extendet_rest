package com.epam.esm.gift_extended.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.CertificateRepository;

@Service
public class CertificateService implements GiftService<Certificate> {

    private final CertificateRepository repository;

    private final TagService tagService;

    private final UserService userService;

    @Override
    public Iterable<Certificate> all() {
        return repository.findAll();
    }

    @Override
    public long countEntities() {
        return repository.count();
    }

    @Override
    public boolean isExist(Certificate t) {
        return repository.isExist(t);
    }

    private final Map<Predicate<Certificate>, BiConsumer<Certificate, Certificate>> giftCertificateUpdateMap;

    public CertificateService(CertificateRepository repository, TagService tagService, UserService userService) {
        giftCertificateUpdateMap = new HashMap<>();

        giftCertificateUpdateMap.put(cert -> cert.getName() != null, (base, patch) -> base.setName(patch.getName()));
        giftCertificateUpdateMap.put(cert -> cert.getDuration() != null,
                (base, patch) -> base.setDuration(patch.getDuration()));
        giftCertificateUpdateMap.put(cert -> cert.getDescription() != null,
                (base, patch) -> base.setDescription(patch.getDescription()));
        giftCertificateUpdateMap.put(cert -> cert.getCreationTime() != null,
                (base, patch) -> base.setCreationTime(patch.getCreationTime()));
        giftCertificateUpdateMap.put(cert -> cert.getHolder() != null, (base, patch) -> base.setHolder(patch.getHolder()));
        giftCertificateUpdateMap.put(cert -> cert.getPrice() != null, (base, patch) -> base.setPrice(patch.getPrice()));
        giftCertificateUpdateMap.put(certificate -> true, (base, patch) -> base.setUpdateTime(new Date()));

        this.repository = repository;
        this.tagService = tagService;
        this.userService = userService;
    }

    public Certificate updateCertFields(Certificate base, Certificate patch) {
        for (var ent : giftCertificateUpdateMap.entrySet()) {
            if (ent.getKey().test(patch)) {
                ent.getValue().accept(base, patch);
            }
        }
        return base;
    }


    public Iterable<Certificate> searchByAnyString(String pattern) {
        return repository.findDistinctByDescriptionContainingAndNameContaining(pattern, pattern);
    }

    public Iterable<Certificate> searchByListOfTagNames(List<String> names) {
        List<Tag> tags = names.stream()
                .map(name -> tagService.findByName(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return repository.findByContainsAllTagNames(tags);
    }

    public Iterable<Certificate> searchByUserAndTag(Integer tagId, Integer userId) {
        Tag tag = tagService.findById(tagId);
        User user = userService.findById(userId);
        return repository.findCertificateByHolderAndTag(user, tag);
    }

    @Override
    public Certificate findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundedException("cert ", id.toString()));
    }

    @Override
    public void save(Certificate certificate) {
        Date nowDate=new Date();
        certificate.setCreationTime(nowDate);
        certificate.setUpdateTime(nowDate);
        certificate.getTags().forEach(tag -> {
            if(!tagService.isExist(tag)){
                tagService.save(tag);
            }
        });
        repository.save(certificate);
    }

    public void update(Certificate patch) {
        Certificate base = repository.findById(patch.getId())
                .orElseThrow(() -> new ResourceNotFoundedException("tag", patch.getId().toString()));
        Certificate updated = updateCertFields(base, patch);
        repository.save(updated);
    }

    @Override
    public void delete(Integer certificateId) {
        repository.findById(certificateId).ifPresent(t -> repository.delete(t));
    }

    @Transactional
    public void attachTag(int tagId, int certId) {
        Optional<Certificate> certificate = repository.findById(certId);
        Tag tag = tagService.findById(tagId);
        certificate.ifPresent(cert -> {
            cert.attachTag(tag);
        });
    }

    @Transactional
    public void detachTag(int tagId, int certId) {

        Optional<Certificate> certificate = repository.findById(certId);
        Tag tag = tagService.findById(tagId);
        certificate.ifPresent(cert -> {
            cert.detachTag(tag);
        });
    }

    public List<Certificate> allWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return repository.findAll(pageable);
    }

    public Iterable<Certificate> findCertificatesByUser(int userId) {
        return repository.findUserCertificates(userId);
    }

    @Transactional
    public void setHolder(Integer certId, User user) {
        repository.findById(certId).ifPresent(cert -> cert.setHolder(user));
    }
}
