package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryAlpha.class);
        CategoryAlpha categoryAlpha1 = getCategoryAlphaSample1();
        CategoryAlpha categoryAlpha2 = new CategoryAlpha();
        assertThat(categoryAlpha1).isNotEqualTo(categoryAlpha2);

        categoryAlpha2.setId(categoryAlpha1.getId());
        assertThat(categoryAlpha1).isEqualTo(categoryAlpha2);

        categoryAlpha2 = getCategoryAlphaSample2();
        assertThat(categoryAlpha1).isNotEqualTo(categoryAlpha2);
    }

    @Test
    void tenantTest() {
        CategoryAlpha categoryAlpha = getCategoryAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        categoryAlpha.setTenant(masterTenantBack);
        assertThat(categoryAlpha.getTenant()).isEqualTo(masterTenantBack);

        categoryAlpha.tenant(null);
        assertThat(categoryAlpha.getTenant()).isNull();
    }
}
