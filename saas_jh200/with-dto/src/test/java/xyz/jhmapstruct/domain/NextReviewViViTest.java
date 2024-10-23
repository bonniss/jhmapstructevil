package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewViVi.class);
        NextReviewViVi nextReviewViVi1 = getNextReviewViViSample1();
        NextReviewViVi nextReviewViVi2 = new NextReviewViVi();
        assertThat(nextReviewViVi1).isNotEqualTo(nextReviewViVi2);

        nextReviewViVi2.setId(nextReviewViVi1.getId());
        assertThat(nextReviewViVi1).isEqualTo(nextReviewViVi2);

        nextReviewViVi2 = getNextReviewViViSample2();
        assertThat(nextReviewViVi1).isNotEqualTo(nextReviewViVi2);
    }

    @Test
    void productTest() {
        NextReviewViVi nextReviewViVi = getNextReviewViViRandomSampleGenerator();
        NextProductViVi nextProductViViBack = getNextProductViViRandomSampleGenerator();

        nextReviewViVi.setProduct(nextProductViViBack);
        assertThat(nextReviewViVi.getProduct()).isEqualTo(nextProductViViBack);

        nextReviewViVi.product(null);
        assertThat(nextReviewViVi.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewViVi nextReviewViVi = getNextReviewViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewViVi.setTenant(masterTenantBack);
        assertThat(nextReviewViVi.getTenant()).isEqualTo(masterTenantBack);

        nextReviewViVi.tenant(null);
        assertThat(nextReviewViVi.getTenant()).isNull();
    }
}
