package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductViTestSamples.*;
import static xyz.jhmapstruct.domain.NextReviewViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewVi.class);
        NextReviewVi nextReviewVi1 = getNextReviewViSample1();
        NextReviewVi nextReviewVi2 = new NextReviewVi();
        assertThat(nextReviewVi1).isNotEqualTo(nextReviewVi2);

        nextReviewVi2.setId(nextReviewVi1.getId());
        assertThat(nextReviewVi1).isEqualTo(nextReviewVi2);

        nextReviewVi2 = getNextReviewViSample2();
        assertThat(nextReviewVi1).isNotEqualTo(nextReviewVi2);
    }

    @Test
    void productTest() {
        NextReviewVi nextReviewVi = getNextReviewViRandomSampleGenerator();
        NextProductVi nextProductViBack = getNextProductViRandomSampleGenerator();

        nextReviewVi.setProduct(nextProductViBack);
        assertThat(nextReviewVi.getProduct()).isEqualTo(nextProductViBack);

        nextReviewVi.product(null);
        assertThat(nextReviewVi.getProduct()).isNull();
    }

    @Test
    void tenantTest() {
        NextReviewVi nextReviewVi = getNextReviewViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextReviewVi.setTenant(masterTenantBack);
        assertThat(nextReviewVi.getTenant()).isEqualTo(masterTenantBack);

        nextReviewVi.tenant(null);
        assertThat(nextReviewVi.getTenant()).isNull();
    }
}
