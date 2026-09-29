package com.elearning.offers.controller;

import com.elearning.offers.dto.OfferRequest;
import com.elearning.offers.dto.OfferResponse;
import com.elearning.offers.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>> getOffers(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(offerService.getOffers(status));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<OfferResponse>> getCourseOffers(@PathVariable Long courseId) {
        return ResponseEntity.ok(offerService.getCourseOffers(courseId));
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<OfferResponse> createOffer(@Valid @RequestBody OfferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offerService.createOffer(request));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfferResponse> approveOffer(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.approveOffer(id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfferResponse> rejectOffer(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.rejectOffer(id));
    }
}
