package com.elearning.ads.repository;

import com.elearning.ads.entity.Ad;
import com.elearning.ads.entity.AdEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdEventRepository extends JpaRepository<AdEvent, Long> {
    List<AdEvent> findByAdAndSessionId(Ad ad, String sessionId);
}
