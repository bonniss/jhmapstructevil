package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployee.class);
        NextEmployee nextEmployee1 = getNextEmployeeSample1();
        NextEmployee nextEmployee2 = new NextEmployee();
        assertThat(nextEmployee1).isNotEqualTo(nextEmployee2);

        nextEmployee2.setId(nextEmployee1.getId());
        assertThat(nextEmployee1).isEqualTo(nextEmployee2);

        nextEmployee2 = getNextEmployeeSample2();
        assertThat(nextEmployee1).isNotEqualTo(nextEmployee2);
    }

    @Test
    void tenantTest() {
        NextEmployee nextEmployee = getNextEmployeeRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployee.setTenant(masterTenantBack);
        assertThat(nextEmployee.getTenant()).isEqualTo(masterTenantBack);

        nextEmployee.tenant(null);
        assertThat(nextEmployee.getTenant()).isNull();
    }
}
