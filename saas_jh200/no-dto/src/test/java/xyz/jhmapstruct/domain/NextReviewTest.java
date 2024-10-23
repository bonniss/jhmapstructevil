package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReview.class);
        NextReview nextReview1 = getNextReviewSample1();
        NextReview nextReview2 = new NextReview();
        assertThat(nextReview1).isNotEqualTo(nextReview2);

        nextReview2.setId(nextReview1.getId());
        assertThat(nextReview1).isEqualTo(nextReview2);

        nextReview2 = getNextReviewSample2();
        assertThat(nextReview1).isNotEqualTo(nextReview2);
    }

    @Test
    void productTest() {
        NextReview nextReview = getNextReviewRandomSampleGenerator();
        NextProduct nextProductBack = getNextProductRandomSampleGenerator();

        nextReview.setProduct(nextProductBack);
        assertThat(nextReview.getProduct()).isEqualTo(nextProductBack);

        nextReview.product(null);
        assertThat(nextReview.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReview nextReview = getNextReviewRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReview.setTenant(masterTenantBack);
        assertThat(nextReview.getTenant()).isEqualTo(masterTenantBack);

        nextReview.tenant(null);
        assertThat(nextReview.getTenant()).isNull();
    }
}
