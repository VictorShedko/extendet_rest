package com.epam.esm.gift_extended.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.UserRepository;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Service
public class UserService implements GiftService<User> {

    private final CertificateService certificateService;

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository, CertificateService certificateService) {
        this.repository = repository;
        this.certificateService = certificateService;
    }

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Transactional
    @Override
    public void delete(Integer userId) {
        repository.findById(userId).ifPresent(repository::delete);
    }

    @Override
    public Iterable<User> all() {
        return repository.findAll();
    }

    @Override
    public long countEntities() {
        return repository.count();
    }

    @Override
    public boolean isExist(User user) {
        return repository.isExist(user);
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundedException("user id ", id.toString()));
    }

    public User findRichestByOrderPriceSum() {
        return repository.findRichestByOrderPriceSum().orElseThrow();
    }

    public User findByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new ResourceNotFoundedException("user name ", name));
    }

    @Override
    public List<User> allWithPagination(int page, int size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public List<User> findByPartOfName(String pattern, Integer page, Integer size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        return repository.findByNameContains(pattern, pageable);
    }
}
