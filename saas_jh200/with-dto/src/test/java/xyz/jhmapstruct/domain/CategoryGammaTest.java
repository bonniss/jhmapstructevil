package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryGammaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryGamma.class);
        CategoryGamma categoryGamma1 = getCategoryGammaSample1();
        CategoryGamma categoryGamma2 = new CategoryGamma();
        assertThat(categoryGamma1).isNotEqualTo(categoryGamma2);

        categoryGamma2.setId(categoryGamma1.getId());
        assertThat(categoryGamma1).isEqualTo(categoryGamma2);

        categoryGamma2 = getCategoryGammaSample2();
        assertThat(categoryGamma1).isNotEqualTo(categoryGamma2);
    }

    @Test
    void tenantTest() {
        CategoryGamma categoryGamma = getCategoryGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        categoryGamma.setTenant(masterTenantBack);
        assertThat(categoryGamma.getTenant()).isEqualTo(masterTenantBack);

        categoryGamma.tenant(null);
        assertThat(categoryGamma.getTenant()).isNull();
    }
}
