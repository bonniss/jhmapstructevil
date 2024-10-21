package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentViViDTO.class);
        ShipmentViViDTO shipmentViViDTO1 = new ShipmentViViDTO();
        shipmentViViDTO1.setId(1L);
        ShipmentViViDTO shipmentViViDTO2 = new ShipmentViViDTO();
        assertThat(shipmentViViDTO1).isNotEqualTo(shipmentViViDTO2);
        shipmentViViDTO2.setId(shipmentViViDTO1.getId());
        assertThat(shipmentViViDTO1).isEqualTo(shipmentViViDTO2);
        shipmentViViDTO2.setId(2L);
        assertThat(shipmentViViDTO1).isNotEqualTo(shipmentViViDTO2);
        shipmentViViDTO1.setId(null);
        assertThat(shipmentViViDTO1).isNotEqualTo(shipmentViViDTO2);
    }
}
