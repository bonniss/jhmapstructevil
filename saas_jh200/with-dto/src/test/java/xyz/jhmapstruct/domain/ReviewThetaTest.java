package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductThetaTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewTheta.class);
        ReviewTheta reviewTheta1 = getReviewThetaSample1();
        ReviewTheta reviewTheta2 = new ReviewTheta();
        assertThat(reviewTheta1).isNotEqualTo(reviewTheta2);

        reviewTheta2.setId(reviewTheta1.getId());
        assertThat(reviewTheta1).isEqualTo(reviewTheta2);

        reviewTheta2 = getReviewThetaSample2();
        assertThat(reviewTheta1).isNotEqualTo(reviewTheta2);
    }

    @Test
    void productTest() {
        ReviewTheta reviewTheta = getReviewThetaRandomSampleGenerator();
        ProductTheta productThetaBack = getProductThetaRandomSampleGenerator();

        reviewTheta.setProduct(productThetaBack);
        assertThat(reviewTheta.getProduct()).isEqualTo(productThetaBack);

        reviewTheta.product(null);
        assertThat(reviewTheta.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        ReviewTheta reviewTheta = getReviewThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        reviewTheta.setTenant(masterTenantBack);
        assertThat(reviewTheta.getTenant()).isEqualTo(masterTenantBack);

        reviewTheta.tenant(null);
        assertThat(reviewTheta.getTenant()).isNull();
    }
}
