package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryGamma.class);
        NextCategoryGamma nextCategoryGamma1 = getNextCategoryGammaSample1();
        NextCategoryGamma nextCategoryGamma2 = new NextCategoryGamma();
        assertThat(nextCategoryGamma1).isNotEqualTo(nextCategoryGamma2);

        nextCategoryGamma2.setId(nextCategoryGamma1.getId());
        assertThat(nextCategoryGamma1).isEqualTo(nextCategoryGamma2);

        nextCategoryGamma2 = getNextCategoryGammaSample2();
        assertThat(nextCategoryGamma1).isNotEqualTo(nextCategoryGamma2);
    }

    @Test
    void tenantTest() {
        NextCategoryGamma nextCategoryGamma = getNextCategoryGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategoryGamma.setTenant(masterTenantBack);
        assertThat(nextCategoryGamma.getTenant()).isEqualTo(masterTenantBack);

        nextCategoryGamma.tenant(null);
        assertThat(nextCategoryGamma.getTenant()).isNull();
    }
}
