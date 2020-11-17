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

    @Autowired
    private GeneratedSaverService saverService;

    @Autowired
    private CertificateService certificateService;

    @GetMapping(value = "/")
    public CollectionModel<EntityModel<Certificate>> allPaged(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        long all = certificateService.pages(size);
        List<Link> links = new ArrayList<>();
        if (page > 0) {
            links.add(linkTo(methodOn(CertificateController.class).allPaged(page - 1, size)).withRel("prev"));
        }
        if (page < all) {
            links.add(linkTo(methodOn(CertificateController.class).allPaged(page - 1, size)).withRel("next"));
        }
        links.add(linkTo(methodOn(CertificateController.class).allPaged(page, size)).withSelfRel());

        return attachLinksToList(certificateService.allWithPagination(page, size), links);
    }

    @GetMapping(value = "/{id}/")
    public Certificate findById(@PathVariable int id) {

        return attachCertLinks(certificateService.findById(id));
    }

    @GetMapping(value = "/byTagsName")
    public CollectionModel<EntityModel<Certificate>> byTagNames(@RequestParam List<String> tagNames) {
        return attachLinksToList(certificateService.searchByListOfTagNames(tagNames),
                List.of(linkTo(methodOn(CertificateController.class).byTagNames(tagNames)).withSelfRel()));
    }

    @GetMapping(value = "/{find}/find")
    public Iterable<Certificate> find(@PathVariable(name = "find") String pattern) {
        return certificateService.searchByAnyString(pattern);
    }

    @GetMapping(value = "/findByUserAndTag")
    public CollectionModel<EntityModel<Certificate>> findByUserAndTag(@RequestParam Integer userId,
            @RequestParam Integer tagId) {
        return attachLinksToList(certificateService.searchByUserAndTag(tagId, userId),
                List.of(linkTo(methodOn(CertificateController.class).findByUserAndTag(userId, tagId)).withSelfRel()));
    }

    @PostMapping(value = "/")
    public void add(@RequestBody Certificate certificate) {
        certificateService.save(certificate);
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
    public CollectionModel<EntityModel<Certificate>> userCerts(@PathVariable int userId) {
        return attachLinksToList(certificateService.findCertificatesByUser(userId),
                List.of(linkTo(methodOn(CertificateController.class).userCerts(userId)).withSelfRel(),
                        linkTo(methodOn(UserController.class).findById(userId)).withRel("holder")));
    }

    @GetMapping(value = "/lol")
    public void lul() {
        saverService.generateEntities(1000, 1000, 10_000);
    }

    private Certificate attachCertLinks(Certificate cert) {
        cert.add(linkTo(methodOn(CertificateController.class).allPaged(0, 10)).withRel("All Certs"));
        if (cert.getHolder() != null) {
            cert.add(linkTo(methodOn(UserController.class).findById(cert.getHolder().getId())).withRel("holder"));
        }
        return cert;
    }

    private CollectionModel<EntityModel<Certificate>> attachLinksToList(Iterable<Certificate> Certs,
            List<Link> thisLinks) {
        List<Certificate> CertsAsList = new ArrayList<>();
        Certs.forEach(CertsAsList::add);
        Iterable<EntityModel<Certificate>> resultCerts = CertsAsList.stream().map(cert -> {
            cert.getTags()
                    .forEach(tag -> tag.add(
                            linkTo(methodOn(TagController.class).detachTag(cert.getId(), tag.getId())).withRel(
                                    "detach")));
            if (cert.getHolder() != null) {
                return EntityModel.of(cert,
                        linkTo(methodOn(CertificateController.class).findById(cert.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).findById(cert.getHolder().getId())).withRel("holder"),
                        linkTo(methodOn(CertificateController.class).allPaged(0, 10)).withRel("Certs"));
            } else {
                return EntityModel.of(cert,
                        linkTo(methodOn(CertificateController.class).findById(cert.getId())).withSelfRel(),
                        linkTo(methodOn(CertificateController.class).allPaged(0, 10)).withRel("Certs"));
            }

        }).collect(Collectors.toList());

        return CollectionModel.of(resultCerts, thisLinks);
    }
}
