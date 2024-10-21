package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.ShipmentViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentVi.class);
        ShipmentVi shipmentVi1 = getShipmentViSample1();
        ShipmentVi shipmentVi2 = new ShipmentVi();
        assertThat(shipmentVi1).isNotEqualTo(shipmentVi2);

        shipmentVi2.setId(shipmentVi1.getId());
        assertThat(shipmentVi1).isEqualTo(shipmentVi2);

        shipmentVi2 = getShipmentViSample2();
        assertThat(shipmentVi1).isNotEqualTo(shipmentVi2);
    }
}
