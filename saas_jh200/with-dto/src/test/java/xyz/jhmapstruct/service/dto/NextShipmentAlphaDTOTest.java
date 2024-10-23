package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentAlphaDTO.class);
        NextShipmentAlphaDTO nextShipmentAlphaDTO1 = new NextShipmentAlphaDTO();
        nextShipmentAlphaDTO1.setId(1L);
        NextShipmentAlphaDTO nextShipmentAlphaDTO2 = new NextShipmentAlphaDTO();
        assertThat(nextShipmentAlphaDTO1).isNotEqualTo(nextShipmentAlphaDTO2);
        nextShipmentAlphaDTO2.setId(nextShipmentAlphaDTO1.getId());
        assertThat(nextShipmentAlphaDTO1).isEqualTo(nextShipmentAlphaDTO2);
        nextShipmentAlphaDTO2.setId(2L);
        assertThat(nextShipmentAlphaDTO1).isNotEqualTo(nextShipmentAlphaDTO2);
        nextShipmentAlphaDTO1.setId(null);
        assertThat(nextShipmentAlphaDTO1).isNotEqualTo(nextShipmentAlphaDTO2);
    }
}
