package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewSigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewSigma.class);
        ReviewSigma reviewSigma1 = getReviewSigmaSample1();
        ReviewSigma reviewSigma2 = new ReviewSigma();
        assertThat(reviewSigma1).isNotEqualTo(reviewSigma2);

        reviewSigma2.setId(reviewSigma1.getId());
        assertThat(reviewSigma1).isEqualTo(reviewSigma2);

        reviewSigma2 = getReviewSigmaSample2();
        assertThat(reviewSigma1).isNotEqualTo(reviewSigma2);
    }

    @Test
    void productTest() {
        ReviewSigma reviewSigma = getReviewSigmaRandomSampleGenerator();
        ProductSigma productSigmaBack = getProductSigmaRandomSampleGenerator();

        reviewSigma.setProduct(productSigmaBack);
        assertThat(reviewSigma.getProduct()).isEqualTo(productSigmaBack);

        reviewSigma.product(null);
        assertThat(reviewSigma.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        ReviewSigma reviewSigma = getReviewSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        reviewSigma.setTenant(masterTenantBack);
        assertThat(reviewSigma.getTenant()).isEqualTo(masterTenantBack);

        reviewSigma.tenant(null);
        assertThat(reviewSigma.getTenant()).isNull();
    }
}
