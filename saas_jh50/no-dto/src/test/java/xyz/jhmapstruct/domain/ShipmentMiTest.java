package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentMi.class);
        ShipmentMi shipmentMi1 = getShipmentMiSample1();
        ShipmentMi shipmentMi2 = new ShipmentMi();
        assertThat(shipmentMi1).isNotEqualTo(shipmentMi2);

        shipmentMi2.setId(shipmentMi1.getId());
        assertThat(shipmentMi1).isEqualTo(shipmentMi2);

        shipmentMi2 = getShipmentMiSample2();
        assertThat(shipmentMi1).isNotEqualTo(shipmentMi2);
    }

    @Test
    void tenantTest() {
        ShipmentMi shipmentMi = getShipmentMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        shipmentMi.setTenant(masterTenantBack);
        assertThat(shipmentMi.getTenant()).isEqualTo(masterTenantBack);

        shipmentMi.tenant(null);
        assertThat(shipmentMi.getTenant()).isNull();
    }
}
