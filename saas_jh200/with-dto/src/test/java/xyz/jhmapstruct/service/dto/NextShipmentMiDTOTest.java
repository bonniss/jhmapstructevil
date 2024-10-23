package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentMiDTO.class);
        NextShipmentMiDTO nextShipmentMiDTO1 = new NextShipmentMiDTO();
        nextShipmentMiDTO1.setId(1L);
        NextShipmentMiDTO nextShipmentMiDTO2 = new NextShipmentMiDTO();
        assertThat(nextShipmentMiDTO1).isNotEqualTo(nextShipmentMiDTO2);
        nextShipmentMiDTO2.setId(nextShipmentMiDTO1.getId());
        assertThat(nextShipmentMiDTO1).isEqualTo(nextShipmentMiDTO2);
        nextShipmentMiDTO2.setId(2L);
        assertThat(nextShipmentMiDTO1).isNotEqualTo(nextShipmentMiDTO2);
        nextShipmentMiDTO1.setId(null);
        assertThat(nextShipmentMiDTO1).isNotEqualTo(nextShipmentMiDTO2);
    }
}
