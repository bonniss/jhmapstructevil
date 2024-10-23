package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeAlpha.class);
        NextEmployeeAlpha nextEmployeeAlpha1 = getNextEmployeeAlphaSample1();
        NextEmployeeAlpha nextEmployeeAlpha2 = new NextEmployeeAlpha();
        assertThat(nextEmployeeAlpha1).isNotEqualTo(nextEmployeeAlpha2);

        nextEmployeeAlpha2.setId(nextEmployeeAlpha1.getId());
        assertThat(nextEmployeeAlpha1).isEqualTo(nextEmployeeAlpha2);

        nextEmployeeAlpha2 = getNextEmployeeAlphaSample2();
        assertThat(nextEmployeeAlpha1).isNotEqualTo(nextEmployeeAlpha2);
    }

    @Test
    void tenantTest() {
        NextEmployeeAlpha nextEmployeeAlpha = getNextEmployeeAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeAlpha.setTenant(masterTenantBack);
        assertThat(nextEmployeeAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeAlpha.tenant(null);
        assertThat(nextEmployeeAlpha.getTenant()).isNull();
    }
}
