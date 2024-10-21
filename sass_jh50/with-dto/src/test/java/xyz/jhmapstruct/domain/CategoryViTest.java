package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryVi.class);
        CategoryVi categoryVi1 = getCategoryViSample1();
        CategoryVi categoryVi2 = new CategoryVi();
        assertThat(categoryVi1).isNotEqualTo(categoryVi2);

        categoryVi2.setId(categoryVi1.getId());
        assertThat(categoryVi1).isEqualTo(categoryVi2);

        categoryVi2 = getCategoryViSample2();
        assertThat(categoryVi1).isNotEqualTo(categoryVi2);
    }

    @Test
    void tenantTest() {
        CategoryVi categoryVi = getCategoryViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        categoryVi.setTenant(masterTenantBack);
        assertThat(categoryVi.getTenant()).isEqualTo(masterTenantBack);

        categoryVi.tenant(null);
        assertThat(categoryVi.getTenant()).isNull();
    }
}
