package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentThetaDTO.class);
        ShipmentThetaDTO shipmentThetaDTO1 = new ShipmentThetaDTO();
        shipmentThetaDTO1.setId(1L);
        ShipmentThetaDTO shipmentThetaDTO2 = new ShipmentThetaDTO();
        assertThat(shipmentThetaDTO1).isNotEqualTo(shipmentThetaDTO2);
        shipmentThetaDTO2.setId(shipmentThetaDTO1.getId());
        assertThat(shipmentThetaDTO1).isEqualTo(shipmentThetaDTO2);
        shipmentThetaDTO2.setId(2L);
        assertThat(shipmentThetaDTO1).isNotEqualTo(shipmentThetaDTO2);
        shipmentThetaDTO1.setId(null);
        assertThat(shipmentThetaDTO1).isNotEqualTo(shipmentThetaDTO2);
    }
}
