package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryMi.class);
        NextCategoryMi nextCategoryMi1 = getNextCategoryMiSample1();
        NextCategoryMi nextCategoryMi2 = new NextCategoryMi();
        assertThat(nextCategoryMi1).isNotEqualTo(nextCategoryMi2);

        nextCategoryMi2.setId(nextCategoryMi1.getId());
        assertThat(nextCategoryMi1).isEqualTo(nextCategoryMi2);

        nextCategoryMi2 = getNextCategoryMiSample2();
        assertThat(nextCategoryMi1).isNotEqualTo(nextCategoryMi2);
    }

    @Test
    void tenantTest() {
        NextCategoryMi nextCategoryMi = getNextCategoryMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategoryMi.setTenant(masterTenantBack);
        assertThat(nextCategoryMi.getTenant()).isEqualTo(masterTenantBack);

        nextCategoryMi.tenant(null);
        assertThat(nextCategoryMi.getTenant()).isNull();
    }
}
