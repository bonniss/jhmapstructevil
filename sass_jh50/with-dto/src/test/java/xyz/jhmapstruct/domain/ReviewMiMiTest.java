package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewMiMi.class);
        ReviewMiMi reviewMiMi1 = getReviewMiMiSample1();
        ReviewMiMi reviewMiMi2 = new ReviewMiMi();
        assertThat(reviewMiMi1).isNotEqualTo(reviewMiMi2);

        reviewMiMi2.setId(reviewMiMi1.getId());
        assertThat(reviewMiMi1).isEqualTo(reviewMiMi2);

        reviewMiMi2 = getReviewMiMiSample2();
        assertThat(reviewMiMi1).isNotEqualTo(reviewMiMi2);
    }

    @Test
    void productTest() {
        ReviewMiMi reviewMiMi = getReviewMiMiRandomSampleGenerator();
        ProductMiMi productMiMiBack = getProductMiMiRandomSampleGenerator();

        reviewMiMi.setProduct(productMiMiBack);
        assertThat(reviewMiMi.getProduct()).isEqualTo(productMiMiBack);

        reviewMiMi.product(null);
        assertThat(reviewMiMi.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        ReviewMiMi reviewMiMi = getReviewMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        reviewMiMi.setTenant(masterTenantBack);
        assertThat(reviewMiMi.getTenant()).isEqualTo(masterTenantBack);

        reviewMiMi.tenant(null);
        assertThat(reviewMiMi.getTenant()).isNull();
    }
}
