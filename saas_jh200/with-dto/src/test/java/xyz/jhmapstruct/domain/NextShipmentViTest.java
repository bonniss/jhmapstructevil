package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentVi.class);
        NextShipmentVi nextShipmentVi1 = getNextShipmentViSample1();
        NextShipmentVi nextShipmentVi2 = new NextShipmentVi();
        assertThat(nextShipmentVi1).isNotEqualTo(nextShipmentVi2);

        nextShipmentVi2.setId(nextShipmentVi1.getId());
        assertThat(nextShipmentVi1).isEqualTo(nextShipmentVi2);

        nextShipmentVi2 = getNextShipmentViSample2();
        assertThat(nextShipmentVi1).isNotEqualTo(nextShipmentVi2);
    }

    @Test
    void tenantTest() {
        NextShipmentVi nextShipmentVi = getNextShipmentViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentVi.setTenant(masterTenantBack);
        assertThat(nextShipmentVi.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentVi.tenant(null);
        assertThat(nextShipmentVi.getTenant()).isNull();
    }
}
