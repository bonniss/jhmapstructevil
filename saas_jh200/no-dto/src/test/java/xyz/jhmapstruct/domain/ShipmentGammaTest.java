package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentGamma.class);
        ShipmentGamma shipmentGamma1 = getShipmentGammaSample1();
        ShipmentGamma shipmentGamma2 = new ShipmentGamma();
        assertThat(shipmentGamma1).isNotEqualTo(shipmentGamma2);

        shipmentGamma2.setId(shipmentGamma1.getId());
        assertThat(shipmentGamma1).isEqualTo(shipmentGamma2);

        shipmentGamma2 = getShipmentGammaSample2();
        assertThat(shipmentGamma1).isNotEqualTo(shipmentGamma2);
    }

    @Test
    void tenantTest() {
        ShipmentGamma shipmentGamma = getShipmentGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        shipmentGamma.setTenant(masterTenantBack);
        assertThat(shipmentGamma.getTenant()).isEqualTo(masterTenantBack);

        shipmentGamma.tenant(null);
        assertThat(shipmentGamma.getTenant()).isNull();
    }
}
