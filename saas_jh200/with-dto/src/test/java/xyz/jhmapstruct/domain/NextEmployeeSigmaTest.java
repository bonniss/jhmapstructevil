package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeSigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeSigma.class);
        NextEmployeeSigma nextEmployeeSigma1 = getNextEmployeeSigmaSample1();
        NextEmployeeSigma nextEmployeeSigma2 = new NextEmployeeSigma();
        assertThat(nextEmployeeSigma1).isNotEqualTo(nextEmployeeSigma2);

        nextEmployeeSigma2.setId(nextEmployeeSigma1.getId());
        assertThat(nextEmployeeSigma1).isEqualTo(nextEmployeeSigma2);

        nextEmployeeSigma2 = getNextEmployeeSigmaSample2();
        assertThat(nextEmployeeSigma1).isNotEqualTo(nextEmployeeSigma2);
    }

    @Test
    void tenantTest() {
        NextEmployeeSigma nextEmployeeSigma = getNextEmployeeSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeSigma.setTenant(masterTenantBack);
        assertThat(nextEmployeeSigma.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeSigma.tenant(null);
        assertThat(nextEmployeeSigma.getTenant()).isNull();
    }
}
