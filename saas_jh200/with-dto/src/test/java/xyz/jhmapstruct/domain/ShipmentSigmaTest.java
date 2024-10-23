package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentSigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentSigma.class);
        ShipmentSigma shipmentSigma1 = getShipmentSigmaSample1();
        ShipmentSigma shipmentSigma2 = new ShipmentSigma();
        assertThat(shipmentSigma1).isNotEqualTo(shipmentSigma2);

        shipmentSigma2.setId(shipmentSigma1.getId());
        assertThat(shipmentSigma1).isEqualTo(shipmentSigma2);

        shipmentSigma2 = getShipmentSigmaSample2();
        assertThat(shipmentSigma1).isNotEqualTo(shipmentSigma2);
    }

    @Test
    void tenantTest() {
        ShipmentSigma shipmentSigma = getShipmentSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        shipmentSigma.setTenant(masterTenantBack);
        assertThat(shipmentSigma.getTenant()).isEqualTo(masterTenantBack);

        shipmentSigma.tenant(null);
        assertThat(shipmentSigma.getTenant()).isNull();
    }
}
