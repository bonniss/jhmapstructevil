package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeMi.class);
        NextEmployeeMi nextEmployeeMi1 = getNextEmployeeMiSample1();
        NextEmployeeMi nextEmployeeMi2 = new NextEmployeeMi();
        assertThat(nextEmployeeMi1).isNotEqualTo(nextEmployeeMi2);

        nextEmployeeMi2.setId(nextEmployeeMi1.getId());
        assertThat(nextEmployeeMi1).isEqualTo(nextEmployeeMi2);

        nextEmployeeMi2 = getNextEmployeeMiSample2();
        assertThat(nextEmployeeMi1).isNotEqualTo(nextEmployeeMi2);
    }

    @Test
    void tenantTest() {
        NextEmployeeMi nextEmployeeMi = getNextEmployeeMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeMi.setTenant(masterTenantBack);
        assertThat(nextEmployeeMi.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeMi.tenant(null);
        assertThat(nextEmployeeMi.getTenant()).isNull();
    }
}
