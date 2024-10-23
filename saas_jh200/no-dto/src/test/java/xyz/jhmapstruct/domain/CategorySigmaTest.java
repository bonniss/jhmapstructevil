package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategorySigmaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategorySigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategorySigma.class);
        CategorySigma categorySigma1 = getCategorySigmaSample1();
        CategorySigma categorySigma2 = new CategorySigma();
        assertThat(categorySigma1).isNotEqualTo(categorySigma2);

        categorySigma2.setId(categorySigma1.getId());
        assertThat(categorySigma1).isEqualTo(categorySigma2);

        categorySigma2 = getCategorySigmaSample2();
        assertThat(categorySigma1).isNotEqualTo(categorySigma2);
    }

    @Test
    void tenantTest() {
        CategorySigma categorySigma = getCategorySigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        categorySigma.setTenant(masterTenantBack);
        assertThat(categorySigma.getTenant()).isEqualTo(masterTenantBack);

        categorySigma.tenant(null);
        assertThat(categorySigma.getTenant()).isNull();
    }
}
