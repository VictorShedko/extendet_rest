package com.epam.esm.gift_extended.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.EntityAlreadyAssignedException;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.CertificateRepository;
import com.epam.esm.gift_extended.service.util.PageSortInfo;
import com.epam.esm.gift_extended.service.util.SortDirection;

@Service
public class CertificateService implements GiftService<Certificate> {

    private final CertificateRepository repository;

    private TagService tagService;

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

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

    private Map<Predicate<Certificate>, BiConsumer<Certificate, Certificate>> giftCertificateUpdateMap;

    @PostConstruct
    public void initHashMap() {
        giftCertificateUpdateMap = new HashMap<>();

        giftCertificateUpdateMap.put(cert -> cert.getName() != null, (base, patch) -> base.setName(patch.getName()));
        giftCertificateUpdateMap.put(cert -> cert.getDuration() != null,
                (base, patch) -> base.setDuration(patch.getDuration()));
        giftCertificateUpdateMap.put(cert -> cert.getDescription() != null,
                (base, patch) -> base.setDescription(patch.getDescription()));
        giftCertificateUpdateMap.put(cert -> cert.getCreationTime() != null,
                (base, patch) -> base.setCreationTime(patch.getCreationTime()));
        giftCertificateUpdateMap.put(cert -> cert.getHolder() != null,
                (base, patch) -> base.setHolder(patch.getHolder()));
        giftCertificateUpdateMap.put(cert -> cert.getTags() != null && cert.getTags().size()>0, (base, patch) -> {
            base.setTags(patch.getTags().stream().map(tag -> {
                if (!tagService.isExist(tag)) {
                    tagService.save(tag);
                    return tag;

                } else {
                    return tagService.findByName(tag.getName()).get();
                }

            }).collect(Collectors.toList()));
        });
        giftCertificateUpdateMap.put(cert -> cert.getPrice() != null, (base, patch) -> base.setPrice(patch.getPrice()));
        giftCertificateUpdateMap.put(certificate -> true, (base, patch) -> base.setUpdateTime(new Date()));
    }

    public CertificateService(CertificateRepository repository) {
        this.repository = repository;
    }

    public Certificate updateCertFields(Certificate base, Certificate patch) {
        for (var ent : giftCertificateUpdateMap.entrySet()) {
            if (ent.getKey().test(patch)) {
                ent.getValue().accept(base, patch);
            }
        }
        return base;
    }

    public List<Certificate> searchByAnyString(String pattern, Integer page, Integer size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        return repository.findDistinctByDescriptionContainingAndNameContaining(pattern, pattern, pageable);
    }

    public List<Certificate> searchByListOfTagNames(List<String> names, Integer page, Integer size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        List<Tag> tags = names.stream()
                .map(tagService::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return repository.findByContainsAllTagNames(tags, pageable);
    }

    public List<Certificate> searchByUserAndTag(Integer tagId, Integer userId, Integer page, Integer size,
            String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        Tag tag = tagService.findById(tagId);
        User user = userService.findById(userId);
        return repository.findCertificateByHolderAndTag(user, tag, pageable);
    }

    @Override
    public Certificate findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundedException("cert ", id.toString()));
    }

    @Transactional
    @Override
    public void save(Certificate certificate) {
        Date nowDate = new Date();
        certificate.setCreationTime(nowDate);
        certificate.setUpdateTime(nowDate);
        certificate.getTags().forEach(tag -> {
            if (!tagService.isExist(tag)) {
                tagService.save(tag);
            }
            tag.setId(tagService.findByName(tag.getName()).get().getId());
        });
        repository.save(certificate);
    }

    @Transactional
    public void update(Certificate patch) {
        Certificate base = repository.findById(patch.getId())
                .orElseThrow(() -> new ResourceNotFoundedException("tag", patch.getId().toString()));
        updateCertFields(base, patch);
    }

    @Override
    public void delete(Integer certificateId) {
        repository.findById(certificateId).ifPresent(repository::delete);
    }

    @Transactional
    public void attachTag(int tagId, int certId) {
        Optional<Certificate> certificate = repository.findById(certId);
        Tag tag = tagService.findById(tagId);
        certificate.ifPresent(cert -> cert.attachTag(tag));
    }

    @Transactional
    public void detachTag(int tagId, int certId) {
        Optional<Certificate> certificate = repository.findById(certId);
        Tag tag = tagService.findById(tagId);
        certificate.ifPresent(cert -> cert.detachTag(tag));
    }

    @Override
    public List<Certificate> allWithPagination(int page, int size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public List<Certificate> findCertificatesByUser(int userId, Integer page, Integer size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        return repository.findUserCertificates(userId, pageable);
    }

    @Transactional
    public void setHolder(Integer certId, User user) {
        repository.findById(certId).ifPresent(certificate -> {
            if (certificate.getHolder() == null) {
                certificate.setHolder(user);
                certificate.setOrderTime(new Date());
            } else {
                throw new EntityAlreadyAssignedException(
                        "tag already assigned to user " + certificate.getHolder().getName());
            }
        });
    }

    public Certificate findByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new ResourceNotFoundedException("added cert", "gen"));
    }
}
