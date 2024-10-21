package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentViDTO.class);
        ShipmentViDTO shipmentViDTO1 = new ShipmentViDTO();
        shipmentViDTO1.setId(1L);
        ShipmentViDTO shipmentViDTO2 = new ShipmentViDTO();
        assertThat(shipmentViDTO1).isNotEqualTo(shipmentViDTO2);
        shipmentViDTO2.setId(shipmentViDTO1.getId());
        assertThat(shipmentViDTO1).isEqualTo(shipmentViDTO2);
        shipmentViDTO2.setId(2L);
        assertThat(shipmentViDTO1).isNotEqualTo(shipmentViDTO2);
        shipmentViDTO1.setId(null);
        assertThat(shipmentViDTO1).isNotEqualTo(shipmentViDTO2);
    }
}
