package com.elearning.ads.controller;

import com.elearning.ads.dto.AdRequest;
import com.elearning.ads.dto.AdResponse;
import com.elearning.ads.service.AdService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ads")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/active")
    public ResponseEntity<List<AdResponse>> getActiveAds(
            @RequestParam String placementSlot,
            @RequestParam(required = false) String role) {
        return ResponseEntity.ok(adService.getActiveAds(placementSlot, role));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdResponse>> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdResponse> createAd(@Valid @RequestBody AdRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adService.createAd(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdResponse> updateAd(@PathVariable Long id, @Valid @RequestBody AdRequest request) {
        return ResponseEntity.ok(adService.updateAd(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/impression")
    public ResponseEntity<Void> trackImpression(@PathVariable Long id, @RequestParam(required = false) String sessionId) {
        adService.trackImpression(id, sessionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/click")
    public ResponseEntity<Void> trackClick(@PathVariable Long id, @RequestParam(required = false) String sessionId) {
        adService.trackClick(id, sessionId);
        return ResponseEntity.ok().build();
    }
}
