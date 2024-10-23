package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategory.class);
        NextCategory nextCategory1 = getNextCategorySample1();
        NextCategory nextCategory2 = new NextCategory();
        assertThat(nextCategory1).isNotEqualTo(nextCategory2);

        nextCategory2.setId(nextCategory1.getId());
        assertThat(nextCategory1).isEqualTo(nextCategory2);

        nextCategory2 = getNextCategorySample2();
        assertThat(nextCategory1).isNotEqualTo(nextCategory2);
    }

    @Test
    void tenantTest() {
        NextCategory nextCategory = getNextCategoryRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategory.setTenant(masterTenantBack);
        assertThat(nextCategory.getTenant()).isEqualTo(masterTenantBack);

        nextCategory.tenant(null);
        assertThat(nextCategory.getTenant()).isNull();
    }
}
