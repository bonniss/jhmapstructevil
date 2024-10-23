package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentViViDTO.class);
        NextShipmentViViDTO nextShipmentViViDTO1 = new NextShipmentViViDTO();
        nextShipmentViViDTO1.setId(1L);
        NextShipmentViViDTO nextShipmentViViDTO2 = new NextShipmentViViDTO();
        assertThat(nextShipmentViViDTO1).isNotEqualTo(nextShipmentViViDTO2);
        nextShipmentViViDTO2.setId(nextShipmentViViDTO1.getId());
        assertThat(nextShipmentViViDTO1).isEqualTo(nextShipmentViViDTO2);
        nextShipmentViViDTO2.setId(2L);
        assertThat(nextShipmentViViDTO1).isNotEqualTo(nextShipmentViViDTO2);
        nextShipmentViViDTO1.setId(null);
        assertThat(nextShipmentViViDTO1).isNotEqualTo(nextShipmentViViDTO2);
    }
}
