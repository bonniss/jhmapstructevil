package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryAlpha.class);
        NextCategoryAlpha nextCategoryAlpha1 = getNextCategoryAlphaSample1();
        NextCategoryAlpha nextCategoryAlpha2 = new NextCategoryAlpha();
        assertThat(nextCategoryAlpha1).isNotEqualTo(nextCategoryAlpha2);

        nextCategoryAlpha2.setId(nextCategoryAlpha1.getId());
        assertThat(nextCategoryAlpha1).isEqualTo(nextCategoryAlpha2);

        nextCategoryAlpha2 = getNextCategoryAlphaSample2();
        assertThat(nextCategoryAlpha1).isNotEqualTo(nextCategoryAlpha2);
    }

    @Test
    void tenantTest() {
        NextCategoryAlpha nextCategoryAlpha = getNextCategoryAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategoryAlpha.setTenant(masterTenantBack);
        assertThat(nextCategoryAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextCategoryAlpha.tenant(null);
        assertThat(nextCategoryAlpha.getTenant()).isNull();
    }
}
