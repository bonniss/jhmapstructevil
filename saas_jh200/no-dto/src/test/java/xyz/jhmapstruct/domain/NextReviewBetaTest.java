package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewBeta.class);
        NextReviewBeta nextReviewBeta1 = getNextReviewBetaSample1();
        NextReviewBeta nextReviewBeta2 = new NextReviewBeta();
        assertThat(nextReviewBeta1).isNotEqualTo(nextReviewBeta2);

        nextReviewBeta2.setId(nextReviewBeta1.getId());
        assertThat(nextReviewBeta1).isEqualTo(nextReviewBeta2);

        nextReviewBeta2 = getNextReviewBetaSample2();
        assertThat(nextReviewBeta1).isNotEqualTo(nextReviewBeta2);
    }

    @Test
    void productTest() {
        NextReviewBeta nextReviewBeta = getNextReviewBetaRandomSampleGenerator();
        NextProductBeta nextProductBetaBack = getNextProductBetaRandomSampleGenerator();

        nextReviewBeta.setProduct(nextProductBetaBack);
        assertThat(nextReviewBeta.getProduct()).isEqualTo(nextProductBetaBack);

        nextReviewBeta.product(null);
        assertThat(nextReviewBeta.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewBeta nextReviewBeta = getNextReviewBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewBeta.setTenant(masterTenantBack);
        assertThat(nextReviewBeta.getTenant()).isEqualTo(masterTenantBack);

        nextReviewBeta.tenant(null);
        assertThat(nextReviewBeta.getTenant()).isNull();
    }
}
