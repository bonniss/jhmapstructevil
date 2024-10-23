package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewMiMi.class);
        NextReviewMiMi nextReviewMiMi1 = getNextReviewMiMiSample1();
        NextReviewMiMi nextReviewMiMi2 = new NextReviewMiMi();
        assertThat(nextReviewMiMi1).isNotEqualTo(nextReviewMiMi2);

        nextReviewMiMi2.setId(nextReviewMiMi1.getId());
        assertThat(nextReviewMiMi1).isEqualTo(nextReviewMiMi2);

        nextReviewMiMi2 = getNextReviewMiMiSample2();
        assertThat(nextReviewMiMi1).isNotEqualTo(nextReviewMiMi2);
    }

    @Test
    void productTest() {
        NextReviewMiMi nextReviewMiMi = getNextReviewMiMiRandomSampleGenerator();
        NextProductMiMi nextProductMiMiBack = getNextProductMiMiRandomSampleGenerator();

        nextReviewMiMi.setProduct(nextProductMiMiBack);
        assertThat(nextReviewMiMi.getProduct()).isEqualTo(nextProductMiMiBack);

        nextReviewMiMi.product(null);
        assertThat(nextReviewMiMi.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewMiMi nextReviewMiMi = getNextReviewMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewMiMi.setTenant(masterTenantBack);
        assertThat(nextReviewMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextReviewMiMi.tenant(null);
        assertThat(nextReviewMiMi.getTenant()).isNull();
    }
}
