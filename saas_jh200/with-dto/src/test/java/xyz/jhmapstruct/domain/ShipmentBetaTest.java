package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentBeta.class);
        ShipmentBeta shipmentBeta1 = getShipmentBetaSample1();
        ShipmentBeta shipmentBeta2 = new ShipmentBeta();
        assertThat(shipmentBeta1).isNotEqualTo(shipmentBeta2);

        shipmentBeta2.setId(shipmentBeta1.getId());
        assertThat(shipmentBeta1).isEqualTo(shipmentBeta2);

        shipmentBeta2 = getShipmentBetaSample2();
        assertThat(shipmentBeta1).isNotEqualTo(shipmentBeta2);
    }

    @Test
    void tenantTest() {
        ShipmentBeta shipmentBeta = getShipmentBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        shipmentBeta.setTenant(masterTenantBack);
        assertThat(shipmentBeta.getTenant()).isEqualTo(masterTenantBack);

        shipmentBeta.tenant(null);
        assertThat(shipmentBeta.getTenant()).isNull();
    }
}
