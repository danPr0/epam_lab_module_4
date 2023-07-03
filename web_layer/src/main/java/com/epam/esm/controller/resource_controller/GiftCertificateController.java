package com.epam.esm.controller.resource_controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.rest.resource_request.AddGcRequest;
import com.epam.esm.rest.resource_request.UpdateGcRequest;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.TransactionFailException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util_service.SortOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controller class responsible for operations with gift certificates.
 *
 * @author Danylo Proshyn
 */

@RestController
@RequestMapping("/gift-certificates")
@Validated
public class GiftCertificateController {

    private final GiftCertificateService gcService;

    private final String resourceCode = "01";
    private final Logger logger       = LogManager.getLogger(GiftCertificateController.class);

    @Autowired
    public GiftCertificateController(GiftCertificateService gcService) {

        this.gcService = gcService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGiftCertificate(@PathVariable long id) {

        Optional<GiftCertificateDTO> gc = gcService.getGiftCertificate(id);

        if (gc.isPresent()) {
            addLinkToResource(gc.get());

            return ResponseEntity.ok(gc.get());
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of("errorMessage", String.format("Requested resource not found (id = %s)", id),
                            "errorCode", "404" + resourceCode));
        }
    }

    @GetMapping
    public ResponseEntity<CollectionModel<GiftCertificateDTO>> getAll(
            @RequestParam(required = false, value = "tagName") Optional<List<String>> tagNames,
            @RequestParam(required = false) Optional<String> namePart,
            @RequestParam(required = false) Optional<String> descriptionPart,
            @RequestParam(required = false) Optional<List<String>> sort, @RequestParam @Positive Integer page,
            @RequestParam @Positive Integer total) {

        Optional<SortOrder> nameOrder       = Optional.empty();
        Optional<SortOrder> createDateOrder = Optional.empty();

        if (sort.isPresent()) {

            if (sort.get().contains("name_asc")) {
                nameOrder = Optional.of(SortOrder.asc);
            } else if (sort.get().contains("name_desc")) {
                nameOrder = Optional.of(SortOrder.desc);
            } else {
                nameOrder = Optional.empty();
            }

            if (sort.get().contains("createDate_asc")) {
                createDateOrder = Optional.of(SortOrder.asc);
            } else if (sort.get().contains("createDate_desc")) {
                createDateOrder = Optional.of(SortOrder.desc);
            } else {
                createDateOrder = Optional.empty();
            }
        }

        List<GiftCertificateDTO> giftCertificates =
                gcService.getAll(page, total, tagNames, namePart, descriptionPart, nameOrder, createDateOrder);

        for (GiftCertificateDTO gc : giftCertificates) {
            addLinkToResource(gc);
        }

        Link link = linkTo(GiftCertificateController.class).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(giftCertificates, link));
    }

    @PostMapping
    public ResponseEntity<Object> addGiftCertificate(@Valid @RequestBody AddGcRequest req) {

        GiftCertificateDTO gc = gcService.addGiftCertificate(
                GiftCertificateDTO.builder().name(req.getName()).description(req.getDescription())
                                  .price(req.getPrice()).duration(req.getDuration()).tags(req.getTags()).build());
        addLinkToResource(gc);

        return ResponseEntity.status(201).body(gc);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateGiftCertificate(
            @PathVariable long id, @Valid @RequestBody UpdateGcRequest req) {

        try {
            GiftCertificateDTO gc = gcService.getGiftCertificate(id).get();
            if (req.getName() != null) {
                gc.setName(req.getName());
            }
            if (req.getDescription() != null) {
                gc.setDescription(req.getDescription());
            }
            if (req.getPrice() != null) {
                gc.setPrice(req.getPrice());
            }
            if (req.getDuration() != null) {
                gc.setDuration(req.getDuration());
            }
            if (req.getTags() != null) {
                gc.setTags(req.getTags());
            }

            GiftCertificateDTO updatedGc = gcService.updateGiftCertificate(gc);
            addLinkToResource(updatedGc);

            return ResponseEntity.ok(updatedGc);
        } catch (NoSuchElementException | TransactionFailException e) {
            String errorMsg = String.format("Resource not found (id = %s)", id);
            logger.error(errorMsg);

            return ResponseEntity.status(404).body(Map.of("errorMessage", errorMsg, "errorCode", "404" + resourceCode));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable long id) {

        if (!gcService.deleteGiftCertificate(id)) {
            return ResponseEntity.status(404)
                    .body(Map.of("errorMessage", String.format("Requested resource not " + "found (id = %s)", id),
                            "errorCode", "404" + resourceCode));
        }

        return ResponseEntity.status(204).build();
    }

    private void addLinkToResource(GiftCertificateDTO gc) {

        gc.add(linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(gc.getId())).withSelfRel());
        for (TagDTO tag : gc.getTags()) {
            tag.add(linkTo(methodOn(TagController.class).getTag(tag.getName())).withSelfRel());
        }
    }
}