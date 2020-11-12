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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.gift_extended.entity.User;
import com.epam.esm.gift_extended.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping(value = "/")
    public User add(@RequestBody User user) {
        service.save(user);
        attachUserLinks(user);
        return user;
    }


    @GetMapping(value = "/")
    public CollectionModel<EntityModel<User>> allPaged(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return attachLinksToList(service.allWithPagination(page, size),
                linkTo(methodOn(UserController.class).allPaged(page, size)).withSelfRel());
    }

    @GetMapping(value = "/{id}/")
    public User findById(@PathVariable int id) {
        User user = service.findById(id);
        user.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
        attachUserLinks(user);
        return user;
    }

    @PostMapping(value = "/{userId}/certs")
    public User setHolder(@PathVariable Integer userId, @RequestBody Integer certId) {
        service.makeOrder(certId, userId);
        User user=service.findById(userId);
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
    public CollectionModel<EntityModel<User>> findByPattern(@PathVariable String pattern) {
        return attachLinksToList(service.findByPartOfName(pattern),
                linkTo(methodOn(UserController.class).findByPattern(pattern)).withSelfRel());
    }

    private User attachUserLinks(User user) {
        user.add(linkTo(methodOn(UserController.class).allPaged(0,10)).withRel("All users"));
        user.add(linkTo(methodOn(CertificateController.class).userCerts(user.getId())).withRel("certs"));
        return user;
    }

    private CollectionModel<EntityModel<User>> attachLinksToList(Iterable<User> users, Link... thisLinks) {
        List<User> usersAsList = new ArrayList<>();
        users.forEach(usersAsList::add);
        Iterable<EntityModel<User>> resultUsers = usersAsList.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel(),
                        linkTo(methodOn(CertificateController.class).userCerts(user.getId())).withRel("certs"),
                        linkTo(methodOn(UserController.class).allPaged(0,10)).withRel("users")))
                .collect(Collectors.toList());

        return CollectionModel.of(resultUsers, thisLinks);
    }
}
