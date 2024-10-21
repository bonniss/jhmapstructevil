package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewMi.class);
        ReviewMi reviewMi1 = getReviewMiSample1();
        ReviewMi reviewMi2 = new ReviewMi();
        assertThat(reviewMi1).isNotEqualTo(reviewMi2);

        reviewMi2.setId(reviewMi1.getId());
        assertThat(reviewMi1).isEqualTo(reviewMi2);

        reviewMi2 = getReviewMiSample2();
        assertThat(reviewMi1).isNotEqualTo(reviewMi2);
    }

    @Test
    void productTest() {
        ReviewMi reviewMi = getReviewMiRandomSampleGenerator();
        ProductMi productMiBack = getProductMiRandomSampleGenerator();

        reviewMi.setProduct(productMiBack);
        assertThat(reviewMi.getProduct()).isEqualTo(productMiBack);

        reviewMi.product(null);
        assertThat(reviewMi.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        ReviewMi reviewMi = getReviewMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        reviewMi.setTenant(masterTenantBack);
        assertThat(reviewMi.getTenant()).isEqualTo(masterTenantBack);

        reviewMi.tenant(null);
        assertThat(reviewMi.getTenant()).isNull();
    }
}
