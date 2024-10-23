package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewAlpha.class);
        NextReviewAlpha nextReviewAlpha1 = getNextReviewAlphaSample1();
        NextReviewAlpha nextReviewAlpha2 = new NextReviewAlpha();
        assertThat(nextReviewAlpha1).isNotEqualTo(nextReviewAlpha2);

        nextReviewAlpha2.setId(nextReviewAlpha1.getId());
        assertThat(nextReviewAlpha1).isEqualTo(nextReviewAlpha2);

        nextReviewAlpha2 = getNextReviewAlphaSample2();
        assertThat(nextReviewAlpha1).isNotEqualTo(nextReviewAlpha2);
    }

    @Test
    void productTest() {
        NextReviewAlpha nextReviewAlpha = getNextReviewAlphaRandomSampleGenerator();
        NextProductAlpha nextProductAlphaBack = getNextProductAlphaRandomSampleGenerator();

        nextReviewAlpha.setProduct(nextProductAlphaBack);
        assertThat(nextReviewAlpha.getProduct()).isEqualTo(nextProductAlphaBack);

        nextReviewAlpha.product(null);
        assertThat(nextReviewAlpha.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewAlpha nextReviewAlpha = getNextReviewAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewAlpha.setTenant(masterTenantBack);
        assertThat(nextReviewAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextReviewAlpha.tenant(null);
        assertThat(nextReviewAlpha.getTenant()).isNull();
    }
}
