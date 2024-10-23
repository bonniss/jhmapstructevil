package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentViVi.class);
        NextShipmentViVi nextShipmentViVi1 = getNextShipmentViViSample1();
        NextShipmentViVi nextShipmentViVi2 = new NextShipmentViVi();
        assertThat(nextShipmentViVi1).isNotEqualTo(nextShipmentViVi2);

        nextShipmentViVi2.setId(nextShipmentViVi1.getId());
        assertThat(nextShipmentViVi1).isEqualTo(nextShipmentViVi2);

        nextShipmentViVi2 = getNextShipmentViViSample2();
        assertThat(nextShipmentViVi1).isNotEqualTo(nextShipmentViVi2);
    }

    @Test
    void tenantTest() {
        NextShipmentViVi nextShipmentViVi = getNextShipmentViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentViVi.setTenant(masterTenantBack);
        assertThat(nextShipmentViVi.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentViVi.tenant(null);
        assertThat(nextShipmentViVi.getTenant()).isNull();
    }
}
