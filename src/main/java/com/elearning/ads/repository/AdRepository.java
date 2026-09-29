package com.elearning.ads.repository;

import com.elearning.ads.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByPlacementSlotAndIsActiveTrue(String placementSlot);
}
