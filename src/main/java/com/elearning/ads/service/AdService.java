package com.elearning.ads.service;

import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.ads.dto.AdRequest;
import com.elearning.ads.dto.AdResponse;
import com.elearning.ads.entity.Ad;
import com.elearning.ads.entity.AdEvent;
import com.elearning.ads.repository.AdEventRepository;
import com.elearning.ads.repository.AdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AdService {

    private final AdRepository adRepository;
    private final AdEventRepository adEventRepository;

    public AdService(AdRepository adRepository, AdEventRepository adEventRepository) {
        this.adRepository = adRepository;
        this.adEventRepository = adEventRepository;
    }

    @Transactional
    public AdResponse createAd(AdRequest request) {
        Ad ad = new Ad();
        ad.setTitle(request.title());
        ad.setImageUrl(request.imageUrl());
        ad.setTargetUrl(request.targetUrl());
        ad.setPlacementSlot(request.placementSlot());
        ad.setTargetRole(request.targetRole());
        ad.setStartsAt(request.startsAt() != null ? LocalDateTime.parse(request.startsAt()) : null);
        ad.setEndsAt(request.endsAt() != null ? LocalDateTime.parse(request.endsAt()) : null);
        ad.setIsActive(request.isActive() != null && request.isActive());

        adRepository.save(ad);
        return toResponse(ad);
    }

    public List<AdResponse> getActiveAds(String placementSlot, String role) {
        return adRepository.findByPlacementSlotAndIsActiveTrue(placementSlot).stream()
            .filter(ad -> ad.getTargetRole() == null || ad.getTargetRole().equals(role))
            .filter(ad -> ad.getStartsAt() == null || ad.getStartsAt().isBefore(LocalDateTime.now()))
            .filter(ad -> ad.getEndsAt() == null || ad.getEndsAt().isAfter(LocalDateTime.now()))
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public void trackImpression(Long adId, String sessionId) {
        Ad ad = adRepository.findById(adId)
            .orElseThrow(() -> new NotFoundException("Ad not found"));

        String effectiveSessionId = sessionId != null ? sessionId : UUID.randomUUID().toString();
        boolean alreadyTracked = adEventRepository.findByAdAndSessionId(ad, effectiveSessionId).stream()
            .anyMatch(e -> "IMPRESSION".equals(e.getEventType()));

        if (!alreadyTracked) {
            AdEvent event = new AdEvent();
            event.setAd(ad);
            event.setEventType("IMPRESSION");
            event.setSessionId(effectiveSessionId);
            adEventRepository.save(event);

            ad.setImpressions(ad.getImpressions() + 1);
            adRepository.save(ad);
        }
    }

    @Transactional
    public void trackClick(Long adId, String sessionId) {
        Ad ad = adRepository.findById(adId)
            .orElseThrow(() -> new NotFoundException("Ad not found"));

        AdEvent event = new AdEvent();
        event.setAd(ad);
        event.setEventType("CLICK");
        event.setSessionId(sessionId);
        adEventRepository.save(event);

        ad.setClicks(ad.getClicks() + 1);
        adRepository.save(ad);
    }

    public List<AdResponse> getAllAds() {
        return adRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public AdResponse updateAd(Long id, AdRequest request) {
        Ad ad = adRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ad not found"));

        ad.setTitle(request.title());
        ad.setImageUrl(request.imageUrl());
        ad.setTargetUrl(request.targetUrl());
        ad.setPlacementSlot(request.placementSlot());
        ad.setTargetRole(request.targetRole());
        ad.setIsActive(request.isActive() != null && request.isActive());

        adRepository.save(ad);
        return toResponse(ad);
    }

    @Transactional
    public void deleteAd(Long id) {
        Ad ad = adRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ad not found"));
        adRepository.delete(ad);
    }

    private AdResponse toResponse(Ad ad) {
        return new AdResponse(ad.getId(), ad.getTitle(), ad.getImageUrl(), ad.getTargetUrl(),
            ad.getPlacementSlot(), ad.getTargetRole(), ad.getStartsAt(), ad.getEndsAt(),
            ad.getIsActive(), ad.getImpressions(), ad.getClicks(), ad.getCreatedAt());
    }
}
