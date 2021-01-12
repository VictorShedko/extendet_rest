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
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("api/gift-certs")
public class CertificateController {

    private final CertificateService certificateService;

    private GeneratedSaverService generatedSaverService;

    @Autowired
    public void setGeneratedSaverService(GeneratedSaverService generatedSaverService) {
        this.generatedSaverService = generatedSaverService;
    }

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/")
    public CollectionModel<EntityModel<Certificate>> allPaged(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        long all = certificateService.pages(size);
        List<Link> links = new ArrayList<>();
        if (page > 0) {
            links.add(linkTo(methodOn(CertificateController.class).allPaged(page - 1, size, sort)).withRel("prev"));
        }
        if (page < all) {
            links.add(linkTo(methodOn(CertificateController.class).allPaged(page + 1, size, sort)).withRel("next"));
        }
        links.add(linkTo(methodOn(CertificateController.class).allPaged(0, size, sort)).withRel("first"));
        links.add(linkTo(methodOn(CertificateController.class).allPaged((int) all, size, sort)).withRel("last"));
        links.add(linkTo(methodOn(CertificateController.class).allPaged(page, size, sort)).withSelfRel());

        return attachLinksToList(certificateService.allWithPagination(page, size, sort), links);
    }

    @GetMapping(value = "/{id}/")
    public Certificate findById(@PathVariable int id) {

        return attachCertLinks(certificateService.findById(id));
    }

    @GetMapping(value = "/byTagsName")
    public CollectionModel<EntityModel<Certificate>> byTagNames(@RequestParam List<String> tagNames,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        return attachLinksToList(certificateService.searchByListOfTagNames(tagNames, page, size, sort), List.of(linkTo(
                methodOn(CertificateController.class).byTagNames(tagNames, page, size, sort)).withSelfRel()));
    }

    @GetMapping(value = "/{find}/find")
    public CollectionModel<EntityModel<Certificate>> find(@PathVariable(name = "find") String pattern,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        return attachLinksToList(certificateService.searchByAnyString(pattern, page, size, sort),
                List.of(linkTo(methodOn(CertificateController.class).find(pattern, page, size, sort)).withSelfRel()));
    }

    @GetMapping(value = "/findByUserAndTag")
    public CollectionModel<EntityModel<Certificate>> findByUserAndTag(@RequestParam Integer userId,
            @RequestParam Integer tagId, @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        return attachLinksToList(certificateService.searchByUserAndTag(tagId, userId, page, size, sort), List.of(linkTo(
                methodOn(CertificateController.class).findByUserAndTag(userId, tagId, page, size,
                        sort)).withSelfRel()));
    }

    @PostMapping(value = "/")
    public Certificate add(@RequestBody Certificate certificate) {

        certificateService.save(certificate);
        return attachCertLinks(certificateService.findByName(certificate.getName()));
    }

    @PatchMapping(value = "/")
    public void update(@RequestBody Certificate certificate) {
        certificateService.update(certificate);
    }

    @DeleteMapping(value = "/{certificateId}/")
    public void delete(@PathVariable int certificateId) {
        certificateService.delete(certificateId);
    }

    @GetMapping(value = "/{userId}/user")
    public CollectionModel<EntityModel<Certificate>> userCerts(@PathVariable int userId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String sort) {
        return attachLinksToList(certificateService.findCertificatesByUser(userId, page, size, sort),
                List.of(linkTo(methodOn(CertificateController.class).userCerts(userId, page, size, sort)).withSelfRel(),
                        linkTo(methodOn(UserController.class).findById(userId)).withRel("holder")));
    }

    @GetMapping(value = "/genrate")
    public void userCerts() {
        generatedSaverService.generateEntities(1000,1000,10000);
    }

    private Certificate attachCertLinks(Certificate cert) {
        cert.add(linkTo(methodOn(CertificateController.class).allPaged(0, 10, "asc")).withRel("All Certs"));
        cert.add(linkTo(methodOn(CertificateController.class).findById(cert.getId())).withRel("cert detail"));
        return cert;
    }

    private CollectionModel<EntityModel<Certificate>> attachLinksToList(Iterable<Certificate> certs,
            List<Link> thisLinks) {
        List<Certificate> CertsAsList = new ArrayList<>();
        certs.forEach(CertsAsList::add);
        Iterable<EntityModel<Certificate>> resultCerts = CertsAsList.stream()
                .map(cert -> EntityModel.of(cert,
                        linkTo(methodOn(CertificateController.class).findById(cert.getId())).withSelfRel(),
                        linkTo(methodOn(CertificateController.class).allPaged(0, 10, "asc")).withRel("Certs")))
                .collect(Collectors.toList());

        return CollectionModel.of(resultCerts, thisLinks);
    }


}
