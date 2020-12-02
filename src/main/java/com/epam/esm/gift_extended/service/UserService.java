package com.epam.esm.gift_extended.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.RegistrationRequest;
import com.epam.esm.gift_extended.entity.Role;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.UserRepository;
import com.epam.esm.gift_extended.repository.UserRepositoryImpl;
import com.epam.esm.gift_extended.security.JWTProvider;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Service
public class UserService implements GiftService<User> {

    private final CertificateService certificateService;

    private final UserRepository repository;

    private PasswordEncoder passwordEncoder;

    private JWTProvider tokenProvider;

    @Autowired
    public UserService(UserRepository repository, CertificateService certificateService) {
        this.repository = repository;
        this.certificateService = certificateService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTokenProvider(JWTProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Autowired
    public UserService(UserRepositoryImpl repository) {
        this.repository = repository;

    }

    @Override
    public void save(User user) {
        String encoded=passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        repository.save(user);
    }

    public void save(RegistrationRequest request) {
        User user = new User();
        user.setName(request.getUsername());
        String encoded=passwordEncoder.encode(request.getPassword());
        user.setPassword(encoded);
        user.setRole(Role.USER);
        repository.save(user);
    }

    public String auth(RegistrationRequest request) {
        User userFromBd = repository.findByName(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundedException("user with username ", request.getUsername()));
        passwordEncoder.matches(userFromBd.getPassword(), request.getUsername());
        return tokenProvider.generateToken(userFromBd.getName());

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
    public List<User> allWithPagination(int page, int size, String sort) {
        PageSortInfo pageable = PageSortInfo.of(page, size, sort);
        return repository.findAll(pageable);
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
