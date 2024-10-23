package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentSigmaDTO.class);
        ShipmentSigmaDTO shipmentSigmaDTO1 = new ShipmentSigmaDTO();
        shipmentSigmaDTO1.setId(1L);
        ShipmentSigmaDTO shipmentSigmaDTO2 = new ShipmentSigmaDTO();
        assertThat(shipmentSigmaDTO1).isNotEqualTo(shipmentSigmaDTO2);
        shipmentSigmaDTO2.setId(shipmentSigmaDTO1.getId());
        assertThat(shipmentSigmaDTO1).isEqualTo(shipmentSigmaDTO2);
        shipmentSigmaDTO2.setId(2L);
        assertThat(shipmentSigmaDTO1).isNotEqualTo(shipmentSigmaDTO2);
        shipmentSigmaDTO1.setId(null);
        assertThat(shipmentSigmaDTO1).isNotEqualTo(shipmentSigmaDTO2);
    }
}
