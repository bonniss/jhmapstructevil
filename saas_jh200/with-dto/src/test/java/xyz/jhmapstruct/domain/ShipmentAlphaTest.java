package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentAlpha.class);
        ShipmentAlpha shipmentAlpha1 = getShipmentAlphaSample1();
        ShipmentAlpha shipmentAlpha2 = new ShipmentAlpha();
        assertThat(shipmentAlpha1).isNotEqualTo(shipmentAlpha2);

        shipmentAlpha2.setId(shipmentAlpha1.getId());
        assertThat(shipmentAlpha1).isEqualTo(shipmentAlpha2);

        shipmentAlpha2 = getShipmentAlphaSample2();
        assertThat(shipmentAlpha1).isNotEqualTo(shipmentAlpha2);
    }

    @Test
    void tenantTest() {
        ShipmentAlpha shipmentAlpha = getShipmentAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        shipmentAlpha.setTenant(masterTenantBack);
        assertThat(shipmentAlpha.getTenant()).isEqualTo(masterTenantBack);

        shipmentAlpha.tenant(null);
        assertThat(shipmentAlpha.getTenant()).isNull();
    }
}
