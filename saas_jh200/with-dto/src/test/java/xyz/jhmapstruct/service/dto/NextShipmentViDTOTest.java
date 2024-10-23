package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentViDTO.class);
        NextShipmentViDTO nextShipmentViDTO1 = new NextShipmentViDTO();
        nextShipmentViDTO1.setId(1L);
        NextShipmentViDTO nextShipmentViDTO2 = new NextShipmentViDTO();
        assertThat(nextShipmentViDTO1).isNotEqualTo(nextShipmentViDTO2);
        nextShipmentViDTO2.setId(nextShipmentViDTO1.getId());
        assertThat(nextShipmentViDTO1).isEqualTo(nextShipmentViDTO2);
        nextShipmentViDTO2.setId(2L);
        assertThat(nextShipmentViDTO1).isNotEqualTo(nextShipmentViDTO2);
        nextShipmentViDTO1.setId(null);
        assertThat(nextShipmentViDTO1).isNotEqualTo(nextShipmentViDTO2);
    }
}
