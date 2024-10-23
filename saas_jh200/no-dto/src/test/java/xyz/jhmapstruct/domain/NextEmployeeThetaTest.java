package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeTheta.class);
        NextEmployeeTheta nextEmployeeTheta1 = getNextEmployeeThetaSample1();
        NextEmployeeTheta nextEmployeeTheta2 = new NextEmployeeTheta();
        assertThat(nextEmployeeTheta1).isNotEqualTo(nextEmployeeTheta2);

        nextEmployeeTheta2.setId(nextEmployeeTheta1.getId());
        assertThat(nextEmployeeTheta1).isEqualTo(nextEmployeeTheta2);

        nextEmployeeTheta2 = getNextEmployeeThetaSample2();
        assertThat(nextEmployeeTheta1).isNotEqualTo(nextEmployeeTheta2);
    }

    @Test
    void tenantTest() {
        NextEmployeeTheta nextEmployeeTheta = getNextEmployeeThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeTheta.setTenant(masterTenantBack);
        assertThat(nextEmployeeTheta.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeTheta.tenant(null);
        assertThat(nextEmployeeTheta.getTenant()).isNull();
    }
}
