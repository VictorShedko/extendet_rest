package com.epam.esm.gift_extended.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.GiftException;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.UserRepository;

@Service
public class UserService implements GiftService<User> {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserRepository repository;

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Transactional
    @Override
    public void delete(Integer userId) {
        repository.findById(userId).ifPresent(user -> repository.delete(user));
    }

    @Override
    public Iterable<User> all() {
        return repository.findAll();
    }


    @Override
    public Page<User> allWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("name"));
        return repository.findAll(pageable);
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id).orElseThrow(()->new ResourceNotFoundedException("user",id.toString()));
    }

    @Transactional
    public void makeOrder(Integer certId, Integer userId) {
        repository.findById(userId).ifPresent(user -> {
             certificateService.setHolder(certId, user);
        });
    }

    public User findRichestByOrderPriceSum() {
        return repository.findRichestByOrderPriceSum().orElseThrow();
    }

    public User findByName(String name){
        return repository.findByName(name).orElseThrow();
    }

    public List<User> findByPartOfName(String pattern){
        return repository.findByNameContains(pattern);
    }
}
