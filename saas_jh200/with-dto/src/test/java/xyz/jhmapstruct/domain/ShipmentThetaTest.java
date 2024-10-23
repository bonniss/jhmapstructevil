package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentTheta.class);
        ShipmentTheta shipmentTheta1 = getShipmentThetaSample1();
        ShipmentTheta shipmentTheta2 = new ShipmentTheta();
        assertThat(shipmentTheta1).isNotEqualTo(shipmentTheta2);

        shipmentTheta2.setId(shipmentTheta1.getId());
        assertThat(shipmentTheta1).isEqualTo(shipmentTheta2);

        shipmentTheta2 = getShipmentThetaSample2();
        assertThat(shipmentTheta1).isNotEqualTo(shipmentTheta2);
    }

    @Test
    void tenantTest() {
        ShipmentTheta shipmentTheta = getShipmentThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        shipmentTheta.setTenant(masterTenantBack);
        assertThat(shipmentTheta.getTenant()).isEqualTo(masterTenantBack);

        shipmentTheta.tenant(null);
        assertThat(shipmentTheta.getTenant()).isNull();
    }
}
