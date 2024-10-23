package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeVi.class);
        NextEmployeeVi nextEmployeeVi1 = getNextEmployeeViSample1();
        NextEmployeeVi nextEmployeeVi2 = new NextEmployeeVi();
        assertThat(nextEmployeeVi1).isNotEqualTo(nextEmployeeVi2);

        nextEmployeeVi2.setId(nextEmployeeVi1.getId());
        assertThat(nextEmployeeVi1).isEqualTo(nextEmployeeVi2);

        nextEmployeeVi2 = getNextEmployeeViSample2();
        assertThat(nextEmployeeVi1).isNotEqualTo(nextEmployeeVi2);
    }

    @Test
    void tenantTest() {
        NextEmployeeVi nextEmployeeVi = getNextEmployeeViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeVi.setTenant(masterTenantBack);
        assertThat(nextEmployeeVi.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeVi.tenant(null);
        assertThat(nextEmployeeVi.getTenant()).isNull();
    }
}
