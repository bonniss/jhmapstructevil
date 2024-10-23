package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentSigmaDTO.class);
        NextShipmentSigmaDTO nextShipmentSigmaDTO1 = new NextShipmentSigmaDTO();
        nextShipmentSigmaDTO1.setId(1L);
        NextShipmentSigmaDTO nextShipmentSigmaDTO2 = new NextShipmentSigmaDTO();
        assertThat(nextShipmentSigmaDTO1).isNotEqualTo(nextShipmentSigmaDTO2);
        nextShipmentSigmaDTO2.setId(nextShipmentSigmaDTO1.getId());
        assertThat(nextShipmentSigmaDTO1).isEqualTo(nextShipmentSigmaDTO2);
        nextShipmentSigmaDTO2.setId(2L);
        assertThat(nextShipmentSigmaDTO1).isNotEqualTo(nextShipmentSigmaDTO2);
        nextShipmentSigmaDTO1.setId(null);
        assertThat(nextShipmentSigmaDTO1).isNotEqualTo(nextShipmentSigmaDTO2);
    }
}
