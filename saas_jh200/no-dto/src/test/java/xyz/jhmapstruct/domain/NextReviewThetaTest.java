package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewTheta.class);
        NextReviewTheta nextReviewTheta1 = getNextReviewThetaSample1();
        NextReviewTheta nextReviewTheta2 = new NextReviewTheta();
        assertThat(nextReviewTheta1).isNotEqualTo(nextReviewTheta2);

        nextReviewTheta2.setId(nextReviewTheta1.getId());
        assertThat(nextReviewTheta1).isEqualTo(nextReviewTheta2);

        nextReviewTheta2 = getNextReviewThetaSample2();
        assertThat(nextReviewTheta1).isNotEqualTo(nextReviewTheta2);
    }

    @Test
    void productTest() {
        NextReviewTheta nextReviewTheta = getNextReviewThetaRandomSampleGenerator();
        NextProductTheta nextProductThetaBack = getNextProductThetaRandomSampleGenerator();

        nextReviewTheta.setProduct(nextProductThetaBack);
        assertThat(nextReviewTheta.getProduct()).isEqualTo(nextProductThetaBack);

        nextReviewTheta.product(null);
        assertThat(nextReviewTheta.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewTheta nextReviewTheta = getNextReviewThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewTheta.setTenant(masterTenantBack);
        assertThat(nextReviewTheta.getTenant()).isEqualTo(masterTenantBack);

        nextReviewTheta.tenant(null);
        assertThat(nextReviewTheta.getTenant()).isNull();
    }
}
