package org.dcode.artificialswbackend.signup.controller;

import org.dcode.artificialswbackend.signup.entity.Families;
import org.dcode.artificialswbackend.signup.service.FamiliesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/family")
public class FamiliesController {

    private final FamiliesService familiesService;

    public FamiliesController(FamiliesService familiesService) {
        this.familiesService = familiesService;
    }

    @PostMapping
    public ResponseEntity<Families> createFamily() {
        Families newFamily = familiesService.createFamily();
        return ResponseEntity.ok(newFamily);
    }
}
