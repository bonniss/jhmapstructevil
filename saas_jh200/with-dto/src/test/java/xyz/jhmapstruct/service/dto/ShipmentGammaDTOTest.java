package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ShipmentGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShipmentGammaDTO.class);
        ShipmentGammaDTO shipmentGammaDTO1 = new ShipmentGammaDTO();
        shipmentGammaDTO1.setId(1L);
        ShipmentGammaDTO shipmentGammaDTO2 = new ShipmentGammaDTO();
        assertThat(shipmentGammaDTO1).isNotEqualTo(shipmentGammaDTO2);
        shipmentGammaDTO2.setId(shipmentGammaDTO1.getId());
        assertThat(shipmentGammaDTO1).isEqualTo(shipmentGammaDTO2);
        shipmentGammaDTO2.setId(2L);
        assertThat(shipmentGammaDTO1).isNotEqualTo(shipmentGammaDTO2);
        shipmentGammaDTO1.setId(null);
        assertThat(shipmentGammaDTO1).isNotEqualTo(shipmentGammaDTO2);
    }
}
