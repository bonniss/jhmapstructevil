package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategorySigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategorySigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategorySigma.class);
        NextCategorySigma nextCategorySigma1 = getNextCategorySigmaSample1();
        NextCategorySigma nextCategorySigma2 = new NextCategorySigma();
        assertThat(nextCategorySigma1).isNotEqualTo(nextCategorySigma2);

        nextCategorySigma2.setId(nextCategorySigma1.getId());
        assertThat(nextCategorySigma1).isEqualTo(nextCategorySigma2);

        nextCategorySigma2 = getNextCategorySigmaSample2();
        assertThat(nextCategorySigma1).isNotEqualTo(nextCategorySigma2);
    }

    @Test
    void tenantTest() {
        NextCategorySigma nextCategorySigma = getNextCategorySigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCategorySigma.setTenant(masterTenantBack);
        assertThat(nextCategorySigma.getTenant()).isEqualTo(masterTenantBack);

        nextCategorySigma.tenant(null);
        assertThat(nextCategorySigma.getTenant()).isNull();
    }
}
