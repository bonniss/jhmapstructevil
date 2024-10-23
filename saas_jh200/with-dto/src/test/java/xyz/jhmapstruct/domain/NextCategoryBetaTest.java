package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryBeta.class);
        NextCategoryBeta nextCategoryBeta1 = getNextCategoryBetaSample1();
        NextCategoryBeta nextCategoryBeta2 = new NextCategoryBeta();
        assertThat(nextCategoryBeta1).isNotEqualTo(nextCategoryBeta2);

        nextCategoryBeta2.setId(nextCategoryBeta1.getId());
        assertThat(nextCategoryBeta1).isEqualTo(nextCategoryBeta2);

        nextCategoryBeta2 = getNextCategoryBetaSample2();
        assertThat(nextCategoryBeta1).isNotEqualTo(nextCategoryBeta2);
    }

    @Test
    void tenantTest() {
        NextCategoryBeta nextCategoryBeta = getNextCategoryBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategoryBeta.setTenant(masterTenantBack);
        assertThat(nextCategoryBeta.getTenant()).isEqualTo(masterTenantBack);

        nextCategoryBeta.tenant(null);
        assertThat(nextCategoryBeta.getTenant()).isNull();
    }
}
