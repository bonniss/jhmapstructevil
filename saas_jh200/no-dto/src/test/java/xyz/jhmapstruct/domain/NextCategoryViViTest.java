package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryViVi.class);
        NextCategoryViVi nextCategoryViVi1 = getNextCategoryViViSample1();
        NextCategoryViVi nextCategoryViVi2 = new NextCategoryViVi();
        assertThat(nextCategoryViVi1).isNotEqualTo(nextCategoryViVi2);

        nextCategoryViVi2.setId(nextCategoryViVi1.getId());
        assertThat(nextCategoryViVi1).isEqualTo(nextCategoryViVi2);

        nextCategoryViVi2 = getNextCategoryViViSample2();
        assertThat(nextCategoryViVi1).isNotEqualTo(nextCategoryViVi2);
    }

    @Test
    void tenantTest() {
        NextCategoryViVi nextCategoryViVi = getNextCategoryViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategoryViVi.setTenant(masterTenantBack);
        assertThat(nextCategoryViVi.getTenant()).isEqualTo(masterTenantBack);

        nextCategoryViVi.tenant(null);
        assertThat(nextCategoryViVi.getTenant()).isNull();
    }
}
