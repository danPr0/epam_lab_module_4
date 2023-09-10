package com.epam.esm.controller.resource_controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.rest.resource_request.AddTagRequest;
import com.epam.esm.service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controller class responsible for operations with tags.
 *
 * @author Danylo Proshyn
 */

@RestController()
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    private static final String RESOURCE_CODE     = "02";
    private static final String ERROR_MESSAGE_KEY = "errorMessage";
    private static final String ERROR_CODE_KEY    = "errorCode";

    @Autowired
    public TagController(TagService tagService) {

        this.tagService = tagService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Object> getTag(@PathVariable String name) {

        Optional<TagDTO> tag = tagService.getTag(name);

        if (tag.isPresent()) {
            addLinkToResource(tag.get());

            return ResponseEntity.ok(tag.get());
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of(ERROR_MESSAGE_KEY, String.format("Requested resource not found (name = %s)", name),
                            ERROR_CODE_KEY, "404" + RESOURCE_CODE));
        }
    }

    @GetMapping("/most-popular-tag")
    public ResponseEntity<Object> getMostPopularUserTag() {

        Optional<TagDTO> tag = tagService.getMostPopularUserTag();

        if (tag.isPresent()) {
            addLinkToResource(tag.get());

            return ResponseEntity.ok(tag.get());
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of(ERROR_MESSAGE_KEY, "Resource not found",
                            ERROR_CODE_KEY, "404" + RESOURCE_CODE));
        }
    }

    @PostMapping()
    public ResponseEntity<Object> addTag(@Valid @RequestBody AddTagRequest req) {

        TagDTO tag = TagDTO.builder().name(req.getName()).build();
        if (tagService.addTag(tag)) {
            addLinkToResource(tag);

            return ResponseEntity.status(201).body(tag);
        } else {
            return ResponseEntity.status(409)
                    .body(Map.of(ERROR_MESSAGE_KEY, String.format("Resource already exists (name = %s)", req.getName()),
                            ERROR_CODE_KEY, "409" + RESOURCE_CODE));
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Object> deleteTag(@PathVariable String name) {

        if (tagService.deleteTag(name)) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(404)
                    .body(Map.of(ERROR_MESSAGE_KEY, String.format("Requested resource not found (name = %s)", name),
                            ERROR_CODE_KEY, "404" + RESOURCE_CODE));
        }
    }

    private void addLinkToResource(TagDTO tag) {

        tag.add(linkTo(methodOn(TagController.class).getTag(tag.getName())).withSelfRel());
    }
}
