package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewSigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewSigma.class);
        NextReviewSigma nextReviewSigma1 = getNextReviewSigmaSample1();
        NextReviewSigma nextReviewSigma2 = new NextReviewSigma();
        assertThat(nextReviewSigma1).isNotEqualTo(nextReviewSigma2);

        nextReviewSigma2.setId(nextReviewSigma1.getId());
        assertThat(nextReviewSigma1).isEqualTo(nextReviewSigma2);

        nextReviewSigma2 = getNextReviewSigmaSample2();
        assertThat(nextReviewSigma1).isNotEqualTo(nextReviewSigma2);
    }

    @Test
    void productTest() {
        NextReviewSigma nextReviewSigma = getNextReviewSigmaRandomSampleGenerator();
        NextProductSigma nextProductSigmaBack = getNextProductSigmaRandomSampleGenerator();

        nextReviewSigma.setProduct(nextProductSigmaBack);
        assertThat(nextReviewSigma.getProduct()).isEqualTo(nextProductSigmaBack);

        nextReviewSigma.product(null);
        assertThat(nextReviewSigma.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewSigma nextReviewSigma = getNextReviewSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewSigma.setTenant(masterTenantBack);
        assertThat(nextReviewSigma.getTenant()).isEqualTo(masterTenantBack);

        nextReviewSigma.tenant(null);
        assertThat(nextReviewSigma.getTenant()).isNull();
    }
}
