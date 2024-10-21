package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryViViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryViVi.class);
        CategoryViVi categoryViVi1 = getCategoryViViSample1();
        CategoryViVi categoryViVi2 = new CategoryViVi();
        assertThat(categoryViVi1).isNotEqualTo(categoryViVi2);

        categoryViVi2.setId(categoryViVi1.getId());
        assertThat(categoryViVi1).isEqualTo(categoryViVi2);

        categoryViVi2 = getCategoryViViSample2();
        assertThat(categoryViVi1).isNotEqualTo(categoryViVi2);
    }

    @Test
    void tenantTest() {
        CategoryViVi categoryViVi = getCategoryViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        categoryViVi.setTenant(masterTenantBack);
        assertThat(categoryViVi.getTenant()).isEqualTo(masterTenantBack);

        categoryViVi.tenant(null);
        assertThat(categoryViVi.getTenant()).isNull();
    }
}
