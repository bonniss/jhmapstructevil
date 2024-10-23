package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewAlpha.class);
        ReviewAlpha reviewAlpha1 = getReviewAlphaSample1();
        ReviewAlpha reviewAlpha2 = new ReviewAlpha();
        assertThat(reviewAlpha1).isNotEqualTo(reviewAlpha2);

        reviewAlpha2.setId(reviewAlpha1.getId());
        assertThat(reviewAlpha1).isEqualTo(reviewAlpha2);

        reviewAlpha2 = getReviewAlphaSample2();
        assertThat(reviewAlpha1).isNotEqualTo(reviewAlpha2);
    }

    @Test
    void productTest() {
        ReviewAlpha reviewAlpha = getReviewAlphaRandomSampleGenerator();
        ProductAlpha productAlphaBack = getProductAlphaRandomSampleGenerator();

        reviewAlpha.setProduct(productAlphaBack);
        assertThat(reviewAlpha.getProduct()).isEqualTo(productAlphaBack);

        reviewAlpha.product(null);
        assertThat(reviewAlpha.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        ReviewAlpha reviewAlpha = getReviewAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        reviewAlpha.setTenant(masterTenantBack);
        assertThat(reviewAlpha.getTenant()).isEqualTo(masterTenantBack);

        reviewAlpha.tenant(null);
        assertThat(reviewAlpha.getTenant()).isNull();
    }
}
