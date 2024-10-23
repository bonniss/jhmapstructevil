package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductGammaTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewGamma.class);
        ReviewGamma reviewGamma1 = getReviewGammaSample1();
        ReviewGamma reviewGamma2 = new ReviewGamma();
        assertThat(reviewGamma1).isNotEqualTo(reviewGamma2);

        reviewGamma2.setId(reviewGamma1.getId());
        assertThat(reviewGamma1).isEqualTo(reviewGamma2);

        reviewGamma2 = getReviewGammaSample2();
        assertThat(reviewGamma1).isNotEqualTo(reviewGamma2);
    }

    @Test
    void productTest() {
        ReviewGamma reviewGamma = getReviewGammaRandomSampleGenerator();
        ProductGamma productGammaBack = getProductGammaRandomSampleGenerator();

        reviewGamma.setProduct(productGammaBack);
        assertThat(reviewGamma.getProduct()).isEqualTo(productGammaBack);

        reviewGamma.product(null);
        assertThat(reviewGamma.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        ReviewGamma reviewGamma = getReviewGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        reviewGamma.setTenant(masterTenantBack);
        assertThat(reviewGamma.getTenant()).isEqualTo(masterTenantBack);

        reviewGamma.tenant(null);
        assertThat(reviewGamma.getTenant()).isNull();
    }
}
