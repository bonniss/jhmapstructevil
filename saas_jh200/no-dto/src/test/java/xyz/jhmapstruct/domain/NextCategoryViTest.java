package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryVi.class);
        NextCategoryVi nextCategoryVi1 = getNextCategoryViSample1();
        NextCategoryVi nextCategoryVi2 = new NextCategoryVi();
        assertThat(nextCategoryVi1).isNotEqualTo(nextCategoryVi2);

        nextCategoryVi2.setId(nextCategoryVi1.getId());
        assertThat(nextCategoryVi1).isEqualTo(nextCategoryVi2);

        nextCategoryVi2 = getNextCategoryViSample2();
        assertThat(nextCategoryVi1).isNotEqualTo(nextCategoryVi2);
    }

    @Test
    void tenantTest() {
        NextCategoryVi nextCategoryVi = getNextCategoryViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategoryVi.setTenant(masterTenantBack);
        assertThat(nextCategoryVi.getTenant()).isEqualTo(masterTenantBack);

        nextCategoryVi.tenant(null);
        assertThat(nextCategoryVi.getTenant()).isNull();
    }
}
