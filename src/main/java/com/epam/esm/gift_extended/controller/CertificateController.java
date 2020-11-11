package com.epam.esm.gift_extended.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.gift_extended.entity.Certificate;
import com.epam.esm.gift_extended.genertors.GeneratedSaverService;
import com.epam.esm.gift_extended.service.CertificateService;

@RestController
@RequestMapping("api/gift-cert")
public class CertificateController {

    @Autowired
    private GeneratedSaverService saverService;

    @Autowired
    private CertificateService certificateService;

    @GetMapping(value = "/certs")
    public Iterable<Certificate> all() {
        return certificateService.all();
    }

    @GetMapping(value = "/certs/pages")
    public Iterable<Certificate> allPaged(@RequestParam Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return certificateService.allWithPagination(page, size);
    }

    @GetMapping(value = "/certs/{tagName}/tagName")
    public Iterable<Certificate> byTag(@PathVariable(name = "tagName") String tagName) {
        return certificateService.searchByTag(tagName);
    }

    @GetMapping(value = "/certs/byTagsName")
    public Iterable<Certificate> byTagNames(@RequestParam List<String> tagNames) {
        return certificateService.searchByListOfTagNames(tagNames);
    }

    @GetMapping(value = "/certs/{find}/find")
    public Iterable<Certificate> find(@PathVariable(name = "find") String pattern) {
        return certificateService.searchByAnyString(pattern);
    }

    @GetMapping(value = "/certs/findByUserAndTag")
    public Iterable<Certificate> findByUserAndTag(@RequestParam Integer userId, @RequestParam Integer tagId) {
        return certificateService.searchByUserAndTag(tagId, userId);
    }

    @GetMapping(value = "/certs/{id}")
    public Certificate certificate(@PathVariable int id) {
        return certificateService.findById(id);
    }

    @PostMapping(value = "/")
    public void add(@RequestBody Certificate certificate) {
        certificateService.save(certificate);
    }

    @PatchMapping(value = "/")
    public void update(@RequestBody Certificate certificate) {
        certificateService.update(certificate);
    }

    @DeleteMapping(value = "/{certificateId}/delete")
    public void delete(@PathVariable int certificateId) {
        certificateService.delete(certificateId);
    }

    @GetMapping(value = "/{userId}/user")
    public Iterable<Certificate> userCerts(@PathVariable int userId) {
        return certificateService.findCertificatesByUser(userId);
    }

    @GetMapping(value = "/lol")
    public void lul() {
        saverService.generateEntities(1000, 1000, 10_000);
    }
}
