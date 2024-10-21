package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.ShipmentViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentViVi.class);
        ShipmentViVi shipmentViVi1 = getShipmentViViSample1();
        ShipmentViVi shipmentViVi2 = new ShipmentViVi();
        assertThat(shipmentViVi1).isNotEqualTo(shipmentViVi2);

        shipmentViVi2.setId(shipmentViVi1.getId());
        assertThat(shipmentViVi1).isEqualTo(shipmentViVi2);

        shipmentViVi2 = getShipmentViViSample2();
        assertThat(shipmentViVi1).isNotEqualTo(shipmentViVi2);
    }
}
