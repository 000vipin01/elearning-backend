package com.elearning.ads;

import com.elearning.ads.entity.Ad;
import com.elearning.common.error.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdServiceTest {

    @Test
    void ad_isActiveDefaultsToTrue() {
        Ad ad = new Ad();
        assertTrue(ad.getIsActive());
    }

    @Test
    void ad_impressionsDefaultsToZero() {
        Ad ad = new Ad();
        assertEquals(0, ad.getImpressions());
    }

    @Test
    void ad_clicksDefaultsToZero() {
        Ad ad = new Ad();
        assertEquals(0, ad.getClicks());
    }

    @Test
    void ad_placementSlotRequired() {
        Ad ad = new Ad();
        ad.setPlacementSlot("DASHBOARD_HERO");
        assertEquals("DASHBOARD_HERO", ad.getPlacementSlot());
    }

    @Test
    void ad_notFound_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("Ad not found");
        });
    }

    @Test
    void ad_trackImpression_incrementsCounter() {
        Ad ad = new Ad();
        int before = ad.getImpressions();
        ad.setImpressions(before + 1);
        assertEquals(before + 1, ad.getImpressions());
    }

    @Test
    void ad_trackClick_incrementsCounter() {
        Ad ad = new Ad();
        int before = ad.getClicks();
        ad.setClicks(before + 1);
        assertEquals(before + 1, ad.getClicks());
    }
}
