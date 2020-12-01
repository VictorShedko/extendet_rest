package com.epam.esm.gift_extended.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.exception.ResourceNotFoundedException;
import com.epam.esm.gift_extended.service.CertificateService;
import com.epam.esm.gift_extended.service.TagService;

@RestControllerAdvice
@RequestMapping("api/tags")
public class TagController {

    private final TagService tagService;

    private final CertificateService certificateService;

    public TagController(TagService tagService, CertificateService certificateService) {
        this.tagService = tagService;
        this.certificateService = certificateService;
    }

    @Deprecated
    @GetMapping(value = "/all")
    public Iterable<Tag> getAllTag() {
        return tagService.all();
    }

    @GetMapping(value = "/{tagId}")
    public Tag findById(@PathVariable int tagId) {
        return attachTagLinks(tagService.findById(tagId));
    }

    @GetMapping(value = "/{tagName}/name")
    public Tag findByName(@PathVariable String tagName) {
        return attachTagLinks(tagService.findByName(tagName)
                .orElseThrow(() -> new ResourceNotFoundedException("tag wit name", tagName)));
    }

    @PostMapping(value = "/")
    public Tag addTag(@RequestBody String tagName) {
        tagService.saveFromString(tagName);
        return attachTagLinks(tagService.findByName(tagName)
                .orElseThrow(() -> new ResourceNotFoundedException("added tag", "new tag")));
    }

    @DeleteMapping(value = "/{tagId}/")
    public void deleteTag(@PathVariable int tagId) {

        tagService.delete(tagId);
    }

    @GetMapping(value = "/{certId}/tags/")
    public CollectionModel<EntityModel<Tag>> tags(@PathVariable int certId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        return attachLinksToList(tagService.tags(certId, page, size, sort),
                List.of(linkTo(methodOn(TagController.class).tags(certId, page, size, sort)).withSelfRel(),
                        linkTo(methodOn(CertificateController.class).findById(certId)).withRel("cert")));
    }

    @PostMapping(value = "/{certId}/tags/{tagId}/")
    public void attachTag(@PathVariable int certId, @PathVariable int tagId) {
        certificateService.attachTag(tagId, certId);
    }

    @DeleteMapping(value = "/{certId}/tags/{tagId}/")
    public void detachTag(@PathVariable int certId, @PathVariable int tagId) {
        certificateService.detachTag(tagId, certId);
    }

    @GetMapping(value = "/")
    public CollectionModel<EntityModel<Tag>> allPaged(@RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        Iterable<Tag> tags = tagService.allWithPagination(page, size, sort);
        long all = tagService.pages(size);
        List<Link> links = new ArrayList<>();
        if (page > 0) {
            links.add(linkTo(methodOn(TagController.class).allPaged(page - 1, size, sort)).withRel("prev"));
        }
        if (page < all) {
            links.add(linkTo(methodOn(TagController.class).allPaged(page + 1, size, sort)).withRel("next"));
        }
        links.add(linkTo(methodOn(CertificateController.class).allPaged(0, size, sort)).withRel("first"));
        links.add(linkTo(methodOn(CertificateController.class).allPaged((int)all, size, sort)).withRel("last"));
        links.add(linkTo(methodOn(TagController.class).allPaged(page, size, sort)).withSelfRel());
        return attachLinksToList(tags, links);
    }

    @GetMapping(value = "mostPopular")
    public Tag mostPopular() {
        return attachTagLinks(tagService.findMostPopular());
    }

    private Tag attachTagLinks(Tag tag) {
        tag.add(linkTo(methodOn(TagController.class).allPaged(0, 10, "asc")).withRel("All tags"));
        tag.add(linkTo(methodOn(CertificateController.class).byTagNames(List.of(tag.getName()), 0, 10, "")).withRel(
                "certs"));
        return tag;
    }

    private CollectionModel<EntityModel<Tag>> attachLinksToList(Iterable<Tag> tags, List<Link> thisLinks) {
        List<Tag> tagsAsList = new ArrayList<>();
        tags.forEach(tagsAsList::add);
        Iterable<EntityModel<Tag>> resultTags = tagsAsList.stream()
                .map(tag -> EntityModel.of(tag,
                        linkTo(methodOn(TagController.class).findById(tag.getId())).withSelfRel(),
                        linkTo(methodOn(CertificateController.class).byTagNames(List.of(tag.getName()), 0, 10,
                                "")).withRel("certs"),
                        linkTo(methodOn(TagController.class).allPaged(0, 10, "asc")).withRel("tags")))
                .collect(Collectors.toList());

        return CollectionModel.of(resultTags, thisLinks);
    }
}
