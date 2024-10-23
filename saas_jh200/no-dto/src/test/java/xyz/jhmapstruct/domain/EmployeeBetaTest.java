package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.EmployeeBetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class EmployeeBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeBeta.class);
        EmployeeBeta employeeBeta1 = getEmployeeBetaSample1();
        EmployeeBeta employeeBeta2 = new EmployeeBeta();
        assertThat(employeeBeta1).isNotEqualTo(employeeBeta2);

        employeeBeta2.setId(employeeBeta1.getId());
        assertThat(employeeBeta1).isEqualTo(employeeBeta2);

        employeeBeta2 = getEmployeeBetaSample2();
        assertThat(employeeBeta1).isNotEqualTo(employeeBeta2);
    }

    @Test
    void tenantTest() {
        EmployeeBeta employeeBeta = getEmployeeBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        employeeBeta.setTenant(masterTenantBack);
        assertThat(employeeBeta.getTenant()).isEqualTo(masterTenantBack);

        employeeBeta.tenant(null);
        assertThat(employeeBeta.getTenant()).isNull();
    }
}
