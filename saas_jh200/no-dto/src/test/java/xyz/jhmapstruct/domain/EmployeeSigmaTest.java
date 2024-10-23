package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeSigma.class);
        EmployeeSigma employeeSigma1 = getEmployeeSigmaSample1();
        EmployeeSigma employeeSigma2 = new EmployeeSigma();
        assertThat(employeeSigma1).isNotEqualTo(employeeSigma2);

        employeeSigma2.setId(employeeSigma1.getId());
        assertThat(employeeSigma1).isEqualTo(employeeSigma2);

        employeeSigma2 = getEmployeeSigmaSample2();
        assertThat(employeeSigma1).isNotEqualTo(employeeSigma2);
    }

    @Test
    void tenantTest() {
        EmployeeSigma employeeSigma = getEmployeeSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        employeeSigma.setTenant(masterTenantBack);
        assertThat(employeeSigma.getTenant()).isEqualTo(masterTenantBack);

        employeeSigma.tenant(null);
        assertThat(employeeSigma.getTenant()).isNull();
    }
}
