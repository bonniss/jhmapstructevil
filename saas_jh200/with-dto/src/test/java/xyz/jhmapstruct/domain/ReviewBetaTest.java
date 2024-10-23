package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductBetaTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewBeta.class);
        ReviewBeta reviewBeta1 = getReviewBetaSample1();
        ReviewBeta reviewBeta2 = new ReviewBeta();
        assertThat(reviewBeta1).isNotEqualTo(reviewBeta2);

        reviewBeta2.setId(reviewBeta1.getId());
        assertThat(reviewBeta1).isEqualTo(reviewBeta2);

        reviewBeta2 = getReviewBetaSample2();
        assertThat(reviewBeta1).isNotEqualTo(reviewBeta2);
    }

    @Test
    void productTest() {
        ReviewBeta reviewBeta = getReviewBetaRandomSampleGenerator();
        ProductBeta productBetaBack = getProductBetaRandomSampleGenerator();

        reviewBeta.setProduct(productBetaBack);
        assertThat(reviewBeta.getProduct()).isEqualTo(productBetaBack);

        reviewBeta.product(null);
        assertThat(reviewBeta.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        ReviewBeta reviewBeta = getReviewBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        reviewBeta.setTenant(masterTenantBack);
        assertThat(reviewBeta.getTenant()).isEqualTo(masterTenantBack);

        reviewBeta.tenant(null);
        assertThat(reviewBeta.getTenant()).isNull();
    }
}
