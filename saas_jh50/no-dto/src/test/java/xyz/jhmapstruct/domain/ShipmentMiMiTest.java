package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentMiMi.class);
        ShipmentMiMi shipmentMiMi1 = getShipmentMiMiSample1();
        ShipmentMiMi shipmentMiMi2 = new ShipmentMiMi();
        assertThat(shipmentMiMi1).isNotEqualTo(shipmentMiMi2);

        shipmentMiMi2.setId(shipmentMiMi1.getId());
        assertThat(shipmentMiMi1).isEqualTo(shipmentMiMi2);

        shipmentMiMi2 = getShipmentMiMiSample2();
        assertThat(shipmentMiMi1).isNotEqualTo(shipmentMiMi2);
    }

    @Test
    void tenantTest() {
        ShipmentMiMi shipmentMiMi = getShipmentMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        shipmentMiMi.setTenant(masterTenantBack);
        assertThat(shipmentMiMi.getTenant()).isEqualTo(masterTenantBack);

        shipmentMiMi.tenant(null);
        assertThat(shipmentMiMi.getTenant()).isNull();
    }
}
