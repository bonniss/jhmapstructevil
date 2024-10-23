package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryMiMi.class);
        NextCategoryMiMi nextCategoryMiMi1 = getNextCategoryMiMiSample1();
        NextCategoryMiMi nextCategoryMiMi2 = new NextCategoryMiMi();
        assertThat(nextCategoryMiMi1).isNotEqualTo(nextCategoryMiMi2);

        nextCategoryMiMi2.setId(nextCategoryMiMi1.getId());
        assertThat(nextCategoryMiMi1).isEqualTo(nextCategoryMiMi2);

        nextCategoryMiMi2 = getNextCategoryMiMiSample2();
        assertThat(nextCategoryMiMi1).isNotEqualTo(nextCategoryMiMi2);
    }

    @Test
    void tenantTest() {
        NextCategoryMiMi nextCategoryMiMi = getNextCategoryMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategoryMiMi.setTenant(masterTenantBack);
        assertThat(nextCategoryMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextCategoryMiMi.tenant(null);
        assertThat(nextCategoryMiMi.getTenant()).isNull();
    }
}
