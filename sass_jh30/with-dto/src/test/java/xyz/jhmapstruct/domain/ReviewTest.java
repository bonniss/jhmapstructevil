package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = getReviewSample1();
        Review review2 = new Review();
        assertThat(review1).isNotEqualTo(review2);

        review2.setId(review1.getId());
        assertThat(review1).isEqualTo(review2);

        review2 = getReviewSample2();
        assertThat(review1).isNotEqualTo(review2);
    }

    @Test
    void productTest() {
        Review review = getReviewRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        review.setProduct(productBack);
        assertThat(review.getProduct()).isEqualTo(productBack);

        review.product(null);
        assertThat(review.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        Review review = getReviewRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        review.setTenant(masterTenantBack);
        assertThat(review.getTenant()).isEqualTo(masterTenantBack);

        review.tenant(null);
        assertThat(review.getTenant()).isNull();
    }
}
