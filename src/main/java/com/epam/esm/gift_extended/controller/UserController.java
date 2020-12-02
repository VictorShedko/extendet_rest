package com.epam.esm.gift_extended.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.gift_extended.entity.RegistrationRequest;
import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.service.CertificateService;
import com.epam.esm.gift_extended.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService service;
    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }

    @PostMapping(value = "/")
    public User add(@RequestBody User user) {
        service.save(user);
        attachUserLinks(user);
        return user;
    }

    @GetMapping(value = "/")
    public CollectionModel<EntityModel<User>> allPaged(@RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc")String sort) {
        List<User> users = service.allWithPagination(page, size, sort);
        long all = service.pages(size);
        List<Link> links = new ArrayList<>();
        if (page > 0) {
            links.add(linkTo(methodOn(TagController.class).allPaged(page - 1, size,sort)).withRel("prev"));
        }
        if (page < all) {
            links.add(linkTo(methodOn(TagController.class).allPaged(page + 1, size,sort)).withRel("next"));
        }
        links.add(linkTo(methodOn(UserController.class).allPaged(0, size, sort)).withRel("first"));
        links.add(linkTo(methodOn(UserController.class).allPaged((int)all, size, sort)).withRel("last"));
        links.add(linkTo(methodOn(UserController.class).allPaged(page, size,sort)).withSelfRel());
        return attachLinksToList(users, links);
    }

    @GetMapping(value = "/{id}/")
    public User findById(@PathVariable int id) {
        User user = service.findById(id);
        user.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
        attachUserLinks(user);
        return user;
    }


    @GetMapping("/richest")
    public User richest() {
        User user = service.findRichestByOrderPriceSum();
        user.add(linkTo(methodOn(UserController.class).richest()).withSelfRel());
        attachUserLinks(user);
        return user;
    }

    @GetMapping("/{userName}/findByName")
    public User findByName(@PathVariable String userName) {
        User user = service.findByName(userName);
        user.add(linkTo(methodOn(UserController.class).findByName(userName)).withSelfRel());
        attachUserLinks(user);
        return user;
    }


    @GetMapping("/{pattern}/findByPattern")
    public CollectionModel<EntityModel<User>> findByPattern(@PathVariable String pattern,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        return attachLinksToList(service.findByPartOfName(pattern,page,size,sort),
                List.of(linkTo(methodOn(UserController.class).findByPattern(pattern,page,size,sort)).withSelfRel()));
    }

    private User attachUserLinks(User user) {
        user.add(linkTo(methodOn(UserController.class).allPaged(0, 10,"asc")).withRel("All users"));
        user.add(linkTo(methodOn(CertificateController.class).userCerts(user.getId(),0,10,"")).withRel("certs"));
        user.add(linkTo(methodOn(UserController.class).findById(user.getId())).withRel("user detail"));
        return user;
    }

    //--------
    //Authentication Endpoints
    //----------------

    @PostMapping("/register")
    public void registerUser(@RequestBody RegistrationRequest registrationRequest) {
        service.save(registrationRequest);
    }

    @PostMapping("/auth")
    public String auth(@RequestBody RegistrationRequest request) {
        String token = service.auth(request);
        return token;
    }

    //--------
    //End of authentication endpoints
    //----------------


    private CollectionModel<EntityModel<User>> attachLinksToList(Iterable<User> users, List<Link> thisLinks) {
        List<User> usersAsList = new ArrayList<>();
        users.forEach(usersAsList::add);
        Iterable<EntityModel<User>> resultUsers = usersAsList.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel(),
                        linkTo(methodOn(CertificateController.class).userCerts(user.getId(),0,10,"")).withRel("certs"),
                        linkTo(methodOn(UserController.class).allPaged(0, 10,"asc")).withRel("users")))
                .collect(Collectors.toList());

        return CollectionModel.of(resultUsers, thisLinks);
    }
}
