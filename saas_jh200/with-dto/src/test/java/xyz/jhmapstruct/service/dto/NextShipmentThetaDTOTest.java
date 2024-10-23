package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentThetaDTO.class);
        NextShipmentThetaDTO nextShipmentThetaDTO1 = new NextShipmentThetaDTO();
        nextShipmentThetaDTO1.setId(1L);
        NextShipmentThetaDTO nextShipmentThetaDTO2 = new NextShipmentThetaDTO();
        assertThat(nextShipmentThetaDTO1).isNotEqualTo(nextShipmentThetaDTO2);
        nextShipmentThetaDTO2.setId(nextShipmentThetaDTO1.getId());
        assertThat(nextShipmentThetaDTO1).isEqualTo(nextShipmentThetaDTO2);
        nextShipmentThetaDTO2.setId(2L);
        assertThat(nextShipmentThetaDTO1).isNotEqualTo(nextShipmentThetaDTO2);
        nextShipmentThetaDTO1.setId(null);
        assertThat(nextShipmentThetaDTO1).isNotEqualTo(nextShipmentThetaDTO2);
    }
}
