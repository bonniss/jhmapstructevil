package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeGamma.class);
        NextEmployeeGamma nextEmployeeGamma1 = getNextEmployeeGammaSample1();
        NextEmployeeGamma nextEmployeeGamma2 = new NextEmployeeGamma();
        assertThat(nextEmployeeGamma1).isNotEqualTo(nextEmployeeGamma2);

        nextEmployeeGamma2.setId(nextEmployeeGamma1.getId());
        assertThat(nextEmployeeGamma1).isEqualTo(nextEmployeeGamma2);

        nextEmployeeGamma2 = getNextEmployeeGammaSample2();
        assertThat(nextEmployeeGamma1).isNotEqualTo(nextEmployeeGamma2);
    }

    @Test
    void tenantTest() {
        NextEmployeeGamma nextEmployeeGamma = getNextEmployeeGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeGamma.setTenant(masterTenantBack);
        assertThat(nextEmployeeGamma.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeGamma.tenant(null);
        assertThat(nextEmployeeGamma.getTenant()).isNull();
    }
}
