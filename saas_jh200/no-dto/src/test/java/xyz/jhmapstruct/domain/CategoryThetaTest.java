package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryThetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryTheta.class);
        CategoryTheta categoryTheta1 = getCategoryThetaSample1();
        CategoryTheta categoryTheta2 = new CategoryTheta();
        assertThat(categoryTheta1).isNotEqualTo(categoryTheta2);

        categoryTheta2.setId(categoryTheta1.getId());
        assertThat(categoryTheta1).isEqualTo(categoryTheta2);

        categoryTheta2 = getCategoryThetaSample2();
        assertThat(categoryTheta1).isNotEqualTo(categoryTheta2);
    }

    @Test
    void tenantTest() {
        CategoryTheta categoryTheta = getCategoryThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        categoryTheta.setTenant(masterTenantBack);
        assertThat(categoryTheta.getTenant()).isEqualTo(masterTenantBack);

        categoryTheta.tenant(null);
        assertThat(categoryTheta.getTenant()).isNull();
    }
}
