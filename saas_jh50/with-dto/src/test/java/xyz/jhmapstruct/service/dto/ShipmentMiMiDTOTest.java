package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentMiMiDTO.class);
        ShipmentMiMiDTO shipmentMiMiDTO1 = new ShipmentMiMiDTO();
        shipmentMiMiDTO1.setId(1L);
        ShipmentMiMiDTO shipmentMiMiDTO2 = new ShipmentMiMiDTO();
        assertThat(shipmentMiMiDTO1).isNotEqualTo(shipmentMiMiDTO2);
        shipmentMiMiDTO2.setId(shipmentMiMiDTO1.getId());
        assertThat(shipmentMiMiDTO1).isEqualTo(shipmentMiMiDTO2);
        shipmentMiMiDTO2.setId(2L);
        assertThat(shipmentMiMiDTO1).isNotEqualTo(shipmentMiMiDTO2);
        shipmentMiMiDTO1.setId(null);
        assertThat(shipmentMiMiDTO1).isNotEqualTo(shipmentMiMiDTO2);
    }
}
