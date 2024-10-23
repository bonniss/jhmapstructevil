package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentAlphaDTO.class);
        ShipmentAlphaDTO shipmentAlphaDTO1 = new ShipmentAlphaDTO();
        shipmentAlphaDTO1.setId(1L);
        ShipmentAlphaDTO shipmentAlphaDTO2 = new ShipmentAlphaDTO();
        assertThat(shipmentAlphaDTO1).isNotEqualTo(shipmentAlphaDTO2);
        shipmentAlphaDTO2.setId(shipmentAlphaDTO1.getId());
        assertThat(shipmentAlphaDTO1).isEqualTo(shipmentAlphaDTO2);
        shipmentAlphaDTO2.setId(2L);
        assertThat(shipmentAlphaDTO1).isNotEqualTo(shipmentAlphaDTO2);
        shipmentAlphaDTO1.setId(null);
        assertThat(shipmentAlphaDTO1).isNotEqualTo(shipmentAlphaDTO2);
    }
}
