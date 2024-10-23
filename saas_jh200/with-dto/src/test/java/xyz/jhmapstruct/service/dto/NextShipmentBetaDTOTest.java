package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentBetaDTO.class);
        NextShipmentBetaDTO nextShipmentBetaDTO1 = new NextShipmentBetaDTO();
        nextShipmentBetaDTO1.setId(1L);
        NextShipmentBetaDTO nextShipmentBetaDTO2 = new NextShipmentBetaDTO();
        assertThat(nextShipmentBetaDTO1).isNotEqualTo(nextShipmentBetaDTO2);
        nextShipmentBetaDTO2.setId(nextShipmentBetaDTO1.getId());
        assertThat(nextShipmentBetaDTO1).isEqualTo(nextShipmentBetaDTO2);
        nextShipmentBetaDTO2.setId(2L);
        assertThat(nextShipmentBetaDTO1).isNotEqualTo(nextShipmentBetaDTO2);
        nextShipmentBetaDTO1.setId(null);
        assertThat(nextShipmentBetaDTO1).isNotEqualTo(nextShipmentBetaDTO2);
    }
}
