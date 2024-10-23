package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentMiMiDTO.class);
        NextShipmentMiMiDTO nextShipmentMiMiDTO1 = new NextShipmentMiMiDTO();
        nextShipmentMiMiDTO1.setId(1L);
        NextShipmentMiMiDTO nextShipmentMiMiDTO2 = new NextShipmentMiMiDTO();
        assertThat(nextShipmentMiMiDTO1).isNotEqualTo(nextShipmentMiMiDTO2);
        nextShipmentMiMiDTO2.setId(nextShipmentMiMiDTO1.getId());
        assertThat(nextShipmentMiMiDTO1).isEqualTo(nextShipmentMiMiDTO2);
        nextShipmentMiMiDTO2.setId(2L);
        assertThat(nextShipmentMiMiDTO1).isNotEqualTo(nextShipmentMiMiDTO2);
        nextShipmentMiMiDTO1.setId(null);
        assertThat(nextShipmentMiMiDTO1).isNotEqualTo(nextShipmentMiMiDTO2);
    }
}
