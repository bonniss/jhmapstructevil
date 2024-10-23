package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeGammaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeGamma.class);
        EmployeeGamma employeeGamma1 = getEmployeeGammaSample1();
        EmployeeGamma employeeGamma2 = new EmployeeGamma();
        assertThat(employeeGamma1).isNotEqualTo(employeeGamma2);

        employeeGamma2.setId(employeeGamma1.getId());
        assertThat(employeeGamma1).isEqualTo(employeeGamma2);

        employeeGamma2 = getEmployeeGammaSample2();
        assertThat(employeeGamma1).isNotEqualTo(employeeGamma2);
    }

    @Test
    void tenantTest() {
        EmployeeGamma employeeGamma = getEmployeeGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        employeeGamma.setTenant(masterTenantBack);
        assertThat(employeeGamma.getTenant()).isEqualTo(masterTenantBack);

        employeeGamma.tenant(null);
        assertThat(employeeGamma.getTenant()).isNull();
    }
}
