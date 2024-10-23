package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentBetaDTO.class);
        ShipmentBetaDTO shipmentBetaDTO1 = new ShipmentBetaDTO();
        shipmentBetaDTO1.setId(1L);
        ShipmentBetaDTO shipmentBetaDTO2 = new ShipmentBetaDTO();
        assertThat(shipmentBetaDTO1).isNotEqualTo(shipmentBetaDTO2);
        shipmentBetaDTO2.setId(shipmentBetaDTO1.getId());
        assertThat(shipmentBetaDTO1).isEqualTo(shipmentBetaDTO2);
        shipmentBetaDTO2.setId(2L);
        assertThat(shipmentBetaDTO1).isNotEqualTo(shipmentBetaDTO2);
        shipmentBetaDTO1.setId(null);
        assertThat(shipmentBetaDTO1).isNotEqualTo(shipmentBetaDTO2);
    }
}
