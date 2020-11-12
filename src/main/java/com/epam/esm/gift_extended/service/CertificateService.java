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

    @Autowired
    private CertificateRepository repository;

    @Autowired
    private TagService tagService;


    @Autowired
    private UserService userService;

    @Autowired
    public void setRepository(CertificateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Certificate> all() {
        return repository.findAll();
    }

    private final Map<Predicate<Certificate>, BiConsumer<Certificate, Certificate>> giftCertificateUpdateMap;

    public CertificateService() {
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

    public Iterable<Certificate> searchByTag(String name) {
        Optional<Tag> tag = tagService.findByName(name);
        Tag found = tag.orElseThrow(() -> new ResourceNotFoundedException("tag", name));
        return repository.findByContainsAllTagNames(List.of(found));
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
        return repository.findCertificateByHolderAndTags(user, tag);
    }

    @Override
    public Certificate findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundedException("tag", id.toString()));
    }

    @Override
    public void save(Certificate certificate) {
        certificate.setCreationTime(new Date());
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
            repository.save(cert);
        });
    }

    @Transactional
    public void detachTag(int tagId, int certId) {

        Optional<Certificate> certificate = repository.findById(certId);
        Tag tag = tagService.findById(tagId);
        certificate.ifPresent(cert -> {
            cert.detachTag(tag);
            repository.save(cert);
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
