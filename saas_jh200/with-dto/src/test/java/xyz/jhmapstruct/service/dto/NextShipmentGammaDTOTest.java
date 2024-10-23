package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentGammaDTO.class);
        NextShipmentGammaDTO nextShipmentGammaDTO1 = new NextShipmentGammaDTO();
        nextShipmentGammaDTO1.setId(1L);
        NextShipmentGammaDTO nextShipmentGammaDTO2 = new NextShipmentGammaDTO();
        assertThat(nextShipmentGammaDTO1).isNotEqualTo(nextShipmentGammaDTO2);
        nextShipmentGammaDTO2.setId(nextShipmentGammaDTO1.getId());
        assertThat(nextShipmentGammaDTO1).isEqualTo(nextShipmentGammaDTO2);
        nextShipmentGammaDTO2.setId(2L);
        assertThat(nextShipmentGammaDTO1).isNotEqualTo(nextShipmentGammaDTO2);
        nextShipmentGammaDTO1.setId(null);
        assertThat(nextShipmentGammaDTO1).isNotEqualTo(nextShipmentGammaDTO2);
    }
}
