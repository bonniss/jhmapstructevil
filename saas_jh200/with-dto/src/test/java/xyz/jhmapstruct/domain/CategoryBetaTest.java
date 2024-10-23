package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryBetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryBeta.class);
        CategoryBeta categoryBeta1 = getCategoryBetaSample1();
        CategoryBeta categoryBeta2 = new CategoryBeta();
        assertThat(categoryBeta1).isNotEqualTo(categoryBeta2);

        categoryBeta2.setId(categoryBeta1.getId());
        assertThat(categoryBeta1).isEqualTo(categoryBeta2);

        categoryBeta2 = getCategoryBetaSample2();
        assertThat(categoryBeta1).isNotEqualTo(categoryBeta2);
    }

    @Test
    void tenantTest() {
        CategoryBeta categoryBeta = getCategoryBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        categoryBeta.setTenant(masterTenantBack);
        assertThat(categoryBeta.getTenant()).isEqualTo(masterTenantBack);

        categoryBeta.tenant(null);
        assertThat(categoryBeta.getTenant()).isNull();
    }
}
