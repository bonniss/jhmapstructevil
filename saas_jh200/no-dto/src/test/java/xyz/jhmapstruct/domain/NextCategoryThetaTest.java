package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryTheta.class);
        NextCategoryTheta nextCategoryTheta1 = getNextCategoryThetaSample1();
        NextCategoryTheta nextCategoryTheta2 = new NextCategoryTheta();
        assertThat(nextCategoryTheta1).isNotEqualTo(nextCategoryTheta2);

        nextCategoryTheta2.setId(nextCategoryTheta1.getId());
        assertThat(nextCategoryTheta1).isEqualTo(nextCategoryTheta2);

        nextCategoryTheta2 = getNextCategoryThetaSample2();
        assertThat(nextCategoryTheta1).isNotEqualTo(nextCategoryTheta2);
    }

    @Test
    void tenantTest() {
        NextCategoryTheta nextCategoryTheta = getNextCategoryThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategoryTheta.setTenant(masterTenantBack);
        assertThat(nextCategoryTheta.getTenant()).isEqualTo(masterTenantBack);

        nextCategoryTheta.tenant(null);
        assertThat(nextCategoryTheta.getTenant()).isNull();
    }
}
