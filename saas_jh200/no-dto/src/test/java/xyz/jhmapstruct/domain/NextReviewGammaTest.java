package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewGamma.class);
        NextReviewGamma nextReviewGamma1 = getNextReviewGammaSample1();
        NextReviewGamma nextReviewGamma2 = new NextReviewGamma();
        assertThat(nextReviewGamma1).isNotEqualTo(nextReviewGamma2);

        nextReviewGamma2.setId(nextReviewGamma1.getId());
        assertThat(nextReviewGamma1).isEqualTo(nextReviewGamma2);

        nextReviewGamma2 = getNextReviewGammaSample2();
        assertThat(nextReviewGamma1).isNotEqualTo(nextReviewGamma2);
    }

    @Test
    void productTest() {
        NextReviewGamma nextReviewGamma = getNextReviewGammaRandomSampleGenerator();
        NextProductGamma nextProductGammaBack = getNextProductGammaRandomSampleGenerator();

        nextReviewGamma.setProduct(nextProductGammaBack);
        assertThat(nextReviewGamma.getProduct()).isEqualTo(nextProductGammaBack);

        nextReviewGamma.product(null);
        assertThat(nextReviewGamma.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewGamma nextReviewGamma = getNextReviewGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewGamma.setTenant(masterTenantBack);
        assertThat(nextReviewGamma.getTenant()).isEqualTo(masterTenantBack);

        nextReviewGamma.tenant(null);
        assertThat(nextReviewGamma.getTenant()).isNull();
    }
}
