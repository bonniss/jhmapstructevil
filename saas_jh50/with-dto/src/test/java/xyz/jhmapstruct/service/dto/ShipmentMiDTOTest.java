package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentMiDTO.class);
        ShipmentMiDTO shipmentMiDTO1 = new ShipmentMiDTO();
        shipmentMiDTO1.setId(1L);
        ShipmentMiDTO shipmentMiDTO2 = new ShipmentMiDTO();
        assertThat(shipmentMiDTO1).isNotEqualTo(shipmentMiDTO2);
        shipmentMiDTO2.setId(shipmentMiDTO1.getId());
        assertThat(shipmentMiDTO1).isEqualTo(shipmentMiDTO2);
        shipmentMiDTO2.setId(2L);
        assertThat(shipmentMiDTO1).isNotEqualTo(shipmentMiDTO2);
        shipmentMiDTO1.setId(null);
        assertThat(shipmentMiDTO1).isNotEqualTo(shipmentMiDTO2);
    }
}
