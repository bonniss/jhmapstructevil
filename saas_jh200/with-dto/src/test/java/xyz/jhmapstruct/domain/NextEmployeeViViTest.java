package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeViVi.class);
        NextEmployeeViVi nextEmployeeViVi1 = getNextEmployeeViViSample1();
        NextEmployeeViVi nextEmployeeViVi2 = new NextEmployeeViVi();
        assertThat(nextEmployeeViVi1).isNotEqualTo(nextEmployeeViVi2);

        nextEmployeeViVi2.setId(nextEmployeeViVi1.getId());
        assertThat(nextEmployeeViVi1).isEqualTo(nextEmployeeViVi2);

        nextEmployeeViVi2 = getNextEmployeeViViSample2();
        assertThat(nextEmployeeViVi1).isNotEqualTo(nextEmployeeViVi2);
    }

    @Test
    void tenantTest() {
        NextEmployeeViVi nextEmployeeViVi = getNextEmployeeViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeViVi.setTenant(masterTenantBack);
        assertThat(nextEmployeeViVi.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeViVi.tenant(null);
        assertThat(nextEmployeeViVi.getTenant()).isNull();
    }
}
