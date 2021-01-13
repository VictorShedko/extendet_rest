package com.epam.esm.gift_extended.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.esm.gift_extended.entity.RegistrationRequest;
import com.epam.esm.gift_extended.entity.Role;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.exception.InvalidVerificationDataException;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.repository.SpringDataUserRepository;
import com.epam.esm.gift_extended.repository.UserRepository;
import com.epam.esm.gift_extended.repository.UserRepositoryImpl;
import com.epam.esm.gift_extended.security.JWTProvider;
import com.epam.esm.gift_extended.service.util.PageSortInfo;

@Service
public class UserService implements GiftService<User> {

    private static final String SORT_PARAM = "name";
    private final SpringDataUserRepository repository;

    private PasswordEncoder passwordEncoder;

    private JWTProvider tokenProvider;

    @Autowired
    public UserService(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setTokenProvider(JWTProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void save(User user) {
        encodeUser(user);
        repository.save(user);
    }

    public void save(RegistrationRequest request) {
        User user = new User();
        user.setName(request.getUsername());
        user.setPassword(request.getPassword());

        encodeUser(user);

        user.setRole(Role.USER);
        repository.save(user);
    }

    public String auth(RegistrationRequest request) {
        User userFromBd = repository.findByName(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundedException("user with username ", request.getUsername()));
        if (!passwordEncoder.matches(request.getPassword(), userFromBd.getPassword())) {
            throw new InvalidVerificationDataException();
        }
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
        return repository.existsById(user.getId());
    }

    @Override
    public List<User> allWithPagination(int page, int size, String sort) {
        Pageable pageable = PageSortInfo.of(page, size, sort, SORT_PARAM);
        return repository.findAll(pageable).toList();
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

    public List<User> findByPartOfName(String pattern, Integer page, Integer size, String sort) {
        Pageable pageable = PageSortInfo.of(page, size, sort, SORT_PARAM);
        return repository.findByNameContains(pattern, pageable);
    }

    @Transactional
    public void encode() {
        repository.findAll().forEach(user -> {
            if (user.getPassword().length() < 10) {
                encodeUser(user);
            }
        });
    }

    private void encodeUser(User user) {
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
    }
}
