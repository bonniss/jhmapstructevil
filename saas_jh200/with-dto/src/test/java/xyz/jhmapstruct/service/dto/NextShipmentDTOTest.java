package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentDTO.class);
        NextShipmentDTO nextShipmentDTO1 = new NextShipmentDTO();
        nextShipmentDTO1.setId(1L);
        NextShipmentDTO nextShipmentDTO2 = new NextShipmentDTO();
        assertThat(nextShipmentDTO1).isNotEqualTo(nextShipmentDTO2);
        nextShipmentDTO2.setId(nextShipmentDTO1.getId());
        assertThat(nextShipmentDTO1).isEqualTo(nextShipmentDTO2);
        nextShipmentDTO2.setId(2L);
        assertThat(nextShipmentDTO1).isNotEqualTo(nextShipmentDTO2);
        nextShipmentDTO1.setId(null);
        assertThat(nextShipmentDTO1).isNotEqualTo(nextShipmentDTO2);
    }
}
