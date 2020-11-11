package com.epam.esm.gift_extended.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("api/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private CertificateService certificateService;

    @GetMapping(value = "/tags")
    public Iterable<Tag> getAllTag() {
        return tagService.all();
    }

    @GetMapping(value = "/")
    public Tag getTag(@RequestParam int tagId) {

        return tagService.findById(tagId);
    }

    @PostMapping(value = "/")
    public void addTag(@RequestBody String tagName) {

        tagService.add(tagName);
    }

    @DeleteMapping(value = "/{tagId}/delete")
    public void deleteTag(@PathVariable int tagId) {

        tagService.delete(tagId);
    }

    @GetMapping(value = "/{certId}/tags")
    public Iterable<Tag> tags(@PathVariable int certId) {
        return tagService.tags(certId);
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
    public Iterable<Tag> allPaged(@RequestParam Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return tagService.allWithPagination(page, size);
    }

    @GetMapping(value = "mostPopular")
    public Tag mostPopular() {
        return tagService.findMostPopular();
    }
}
