package com.epam.esm.controller.resource_controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.rest.resource_request.AddGcRequest;
import com.epam.esm.rest.resource_request.UpdateGcRequest;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.TransactionFailException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util_service.SortOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
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
@XRayEnabled
public class GiftCertificateController {

    private static final String RESOURCE_CODE     = "01";
    private static final String ERROR_MESSAGE_KEY = "errorMessage";
    private static final String ERROR_CODE_KEY    = "errorCode";


    private final GiftCertificateService gcService;
    private final Logger                 logger = LogManager.getLogger(GiftCertificateController.class);

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
                    .body(Map.of(ERROR_MESSAGE_KEY, String.format("Requested resource not found (id = %s)", id),
                            ERROR_CODE_KEY, "404" + RESOURCE_CODE));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam(required = false, value = "tagName") Optional<List<String>> tagNames,
            @RequestParam(required = false) Optional<String> textFilter,
            @RequestParam(required = false) Optional<List<String>> sort, @RequestParam @Positive Integer page,
            @RequestParam @Positive Integer total) {

        Optional<SortOrder> nameOrder       = Optional.empty();
        Optional<SortOrder> createDateOrder = Optional.empty();

        if (sort.isPresent()) {

            if (sort.get().contains("name_asc")) {
                nameOrder = Optional.of(SortOrder.ASC);
            } else if (sort.get().contains("name_desc")) {
                nameOrder = Optional.of(SortOrder.DESC);
            } else {
                nameOrder = Optional.empty();
            }

            if (sort.get().contains("createDate_asc")) {
                createDateOrder = Optional.of(SortOrder.ASC);
            } else if (sort.get().contains("createDate_desc")) {
                createDateOrder = Optional.of(SortOrder.DESC);
            } else {
                createDateOrder = Optional.empty();
            }
        }

        Pair<List<GiftCertificateDTO>, Integer> gcPage =
                gcService.getAll(page, total, tagNames, textFilter, nameOrder, createDateOrder);

        for (GiftCertificateDTO gc : gcPage.getLeft()) {
            addLinkToResource(gc);
        }

        RepresentationModel<GiftCertificateDTO> model =
                HalModelBuilder
                        .halModel()
                        .embed(gcPage.getLeft(), LinkRelation.of("giftCertificates"))
                        .embed(gcPage.getRight(), LinkRelation.of("totalPages"))
                        .link(linkTo(GiftCertificateController.class).withSelfRel())
                        .build();

        return ResponseEntity.ok(model);
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
            Optional<GiftCertificateDTO> gc = gcService.getGiftCertificate(id);
            if (gc.isEmpty()) {
                throw new NoSuchElementException();
            }
            if (req.getName() != null) {
                gc.get().setName(req.getName());
            }
            if (req.getDescription() != null) {
                gc.get().setDescription(req.getDescription());
            }
            if (req.getPrice() != null) {
                gc.get().setPrice(req.getPrice());
            }
            if (req.getDuration() != null) {
                gc.get().setDuration(req.getDuration());
            }
            if (req.getTags() != null) {
                gc.get().setTags(req.getTags());
            }

            GiftCertificateDTO updatedGc = gcService.updateGiftCertificate(gc.get());
            addLinkToResource(updatedGc);

            return ResponseEntity.ok(updatedGc);
        } catch (NoSuchElementException | TransactionFailException e) {
            String errorMsg = String.format("Resource not found (id = %s)", id);
            logger.error(errorMsg);

            return ResponseEntity.status(404).body(Map.of(ERROR_MESSAGE_KEY, errorMsg, ERROR_CODE_KEY, "404" + RESOURCE_CODE));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGiftCertificate(@PathVariable long id) {

        if (!gcService.deleteGiftCertificate(id)) {
            return ResponseEntity.status(404)
                    .body(Map.of(ERROR_MESSAGE_KEY, String.format("Requested resource not " + "found (id = %s)", id),
                            ERROR_CODE_KEY, "404" + RESOURCE_CODE));
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