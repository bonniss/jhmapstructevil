package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeMiMi.class);
        NextEmployeeMiMi nextEmployeeMiMi1 = getNextEmployeeMiMiSample1();
        NextEmployeeMiMi nextEmployeeMiMi2 = new NextEmployeeMiMi();
        assertThat(nextEmployeeMiMi1).isNotEqualTo(nextEmployeeMiMi2);

        nextEmployeeMiMi2.setId(nextEmployeeMiMi1.getId());
        assertThat(nextEmployeeMiMi1).isEqualTo(nextEmployeeMiMi2);

        nextEmployeeMiMi2 = getNextEmployeeMiMiSample2();
        assertThat(nextEmployeeMiMi1).isNotEqualTo(nextEmployeeMiMi2);
    }

    @Test
    void tenantTest() {
        NextEmployeeMiMi nextEmployeeMiMi = getNextEmployeeMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeMiMi.setTenant(masterTenantBack);
        assertThat(nextEmployeeMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeMiMi.tenant(null);
        assertThat(nextEmployeeMiMi.getTenant()).isNull();
    }
}
