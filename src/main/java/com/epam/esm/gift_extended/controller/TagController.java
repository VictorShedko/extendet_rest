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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.epam.esm.gift_extended.entity.Tag;
import com.epam.esm.gift_extended.service.CertificateService;
import com.epam.esm.gift_extended.service.TagService;

@RestControllerAdvice
@RequestMapping("api/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private CertificateService certificateService;

    @Deprecated
    @GetMapping(value = "/")
    public Iterable<Tag> getAllTag() {
        return tagService.all();
    }

    @GetMapping(value = "/{tagId}")
    public Tag findById(@PathVariable int tagId) {

        return attachTagLinks(tagService.findById(tagId));
    }

    @PostMapping(value = "/")
    public void addTag(@RequestBody String tagName) {

        tagService.add(tagName);
    }

    @DeleteMapping(value = "/{tagId}/")
    public void deleteTag(@PathVariable int tagId) {

        tagService.delete(tagId);
    }

    @GetMapping(value = "/{certId}/tags")
    public CollectionModel<EntityModel<Tag>> tags(@PathVariable int certId) {
        return attachLinksToList(tagService.tags(certId),
                List.of(linkTo(methodOn(TagController.class).tags(certId)).withSelfRel(),
                        linkTo(methodOn(CertificateController.class).findById(certId)).withRel("cert")));
    }

    @PostMapping(value = "/{certId}/tags/{tagId}")
    public void attachTag(@PathVariable int certId, @PathVariable int tagId) {
        certificateService.attachTag(tagId, certId);
    }

    @DeleteMapping(value = "/{certId}/tags/{tagId}/delete")
    public void detachTag(@PathVariable int certId, @PathVariable int tagId) {
        certificateService.detachTag(tagId, certId);
    }

    @GetMapping(value = "/pages")
    public CollectionModel<EntityModel<Tag>> allPaged(@RequestParam Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        Iterable<Tag> tags=tagService.allWithPagination(page, size);
        long all=tagService.pages(size);
        List<Link> links=new ArrayList<>();
        if(page>0){
            links.add( linkTo(methodOn(TagController.class).allPaged(page-1, size)).withRel("prev"));
        }
        if (page<all){
            links.add( linkTo(methodOn(TagController.class).allPaged(page-1, size)).withRel("next"));
        }
        links.add( linkTo(methodOn(TagController.class).allPaged(page, size)).withSelfRel());
        return attachLinksToList(tags,links);
    }

    @GetMapping(value = "mostPopular")
    public Tag mostPopular() {
        return attachTagLinks(tagService.findMostPopular());
    }

    private Tag attachTagLinks(Tag tag) {
        tag.add(linkTo(methodOn(TagController.class).allPaged(0,10)).withRel("All tags"));
        tag.add(linkTo(methodOn(CertificateController.class).byTag(tag.getName())).withRel("certs"));
        return tag;
    }

    private CollectionModel<EntityModel<Tag>> attachLinksToList(Iterable<Tag> tags, List<Link> thisLinks) {
        List<Tag> tagsAsList = new ArrayList<>();
        tags.forEach(tagsAsList::add);
        Iterable<EntityModel<Tag>> resultTags = tagsAsList.stream()
                .map(tag -> EntityModel.of(tag,
                        linkTo(methodOn(TagController.class).findById(tag.getId())).withSelfRel(),
                        linkTo(methodOn(CertificateController.class).byTag(tag.getName())).withRel("certs"),
                        linkTo(methodOn(TagController.class).allPaged(0,10)).withRel("tags")))
                .collect(Collectors.toList());

        return CollectionModel.of(resultTags, thisLinks);
    }
}
