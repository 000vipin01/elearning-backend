package com.elearning.offers.service;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.courses.entity.Course;
import com.elearning.courses.repository.CourseRepository;
import com.elearning.offers.dto.OfferRequest;
import com.elearning.offers.dto.OfferResponse;
import com.elearning.offers.entity.Offer;
import com.elearning.offers.repository.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final CourseRepository courseRepository;

    public OfferService(OfferRepository offerRepository, CourseRepository courseRepository) {
        this.offerRepository = offerRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public OfferResponse createOffer(OfferRequest request) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        Course course = courseRepository.findById(request.courseId())
            .orElseThrow(() -> new NotFoundException("Course not found"));

        if (!course.getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to create offers for this course");
        }

        Offer offer = new Offer();
        offer.setCourse(course);
        offer.setTitle(request.title());
        offer.setDescription(request.description());
        offer.setDiscountPercentage(request.discountPercentage());
        offer.setStartsAt(LocalDateTime.parse(request.startsAt()));
        offer.setEndsAt(LocalDateTime.parse(request.endsAt()));
        offer.setStatus("PENDING");
        offer.setCreatedBy(new com.elearning.users.entity.User() {{ setId(currentUser.id()); }});

        offerRepository.save(offer);
        return toResponse(offer);
    }

    @Transactional
    public OfferResponse approveOffer(Long id) {
        Offer offer = getOfferEntity(id);
        offer.setStatus("APPROVED");
        offerRepository.save(offer);
        return toResponse(offer);
    }

    @Transactional
    public OfferResponse rejectOffer(Long id) {
        Offer offer = getOfferEntity(id);
        offer.setStatus("REJECTED");
        offerRepository.save(offer);
        return toResponse(offer);
    }

    public List<OfferResponse> getOffers(String status) {
        if (status != null) {
            return offerRepository.findByStatus(status).stream().map(this::toResponse).toList();
        }
        return offerRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<OfferResponse> getCourseOffers(Long courseId) {
        return offerRepository.findByCourseId(courseId).stream().map(this::toResponse).toList();
    }

    private Offer getOfferEntity(Long id) {
        return offerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Offer not found"));
    }

    private OfferResponse toResponse(Offer offer) {
        return new OfferResponse(offer.getId(), offer.getCourse().getId(), offer.getTitle(),
            offer.getDescription(), offer.getDiscountPercentage(), offer.getStartsAt(), offer.getEndsAt(),
            offer.getStatus(), offer.getCreatedAt());
    }
}
