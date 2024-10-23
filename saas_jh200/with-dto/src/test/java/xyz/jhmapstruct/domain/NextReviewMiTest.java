package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewMiTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewMi.class);
        NextReviewMi nextReviewMi1 = getNextReviewMiSample1();
        NextReviewMi nextReviewMi2 = new NextReviewMi();
        assertThat(nextReviewMi1).isNotEqualTo(nextReviewMi2);

        nextReviewMi2.setId(nextReviewMi1.getId());
        assertThat(nextReviewMi1).isEqualTo(nextReviewMi2);

        nextReviewMi2 = getNextReviewMiSample2();
        assertThat(nextReviewMi1).isNotEqualTo(nextReviewMi2);
    }

    @Test
    void productTest() {
        NextReviewMi nextReviewMi = getNextReviewMiRandomSampleGenerator();
        ProductMi productMiBack = getProductMiRandomSampleGenerator();

        nextReviewMi.setProduct(productMiBack);
        assertThat(nextReviewMi.getProduct()).isEqualTo(productMiBack);

        nextReviewMi.product(null);
        assertThat(nextReviewMi.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewMi nextReviewMi = getNextReviewMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewMi.setTenant(masterTenantBack);
        assertThat(nextReviewMi.getTenant()).isEqualTo(masterTenantBack);

        nextReviewMi.tenant(null);
        assertThat(nextReviewMi.getTenant()).isNull();
    }
}
