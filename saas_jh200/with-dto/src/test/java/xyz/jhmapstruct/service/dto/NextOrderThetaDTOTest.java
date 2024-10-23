package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderThetaDTO.class);
        NextOrderThetaDTO nextOrderThetaDTO1 = new NextOrderThetaDTO();
        nextOrderThetaDTO1.setId(1L);
        NextOrderThetaDTO nextOrderThetaDTO2 = new NextOrderThetaDTO();
        assertThat(nextOrderThetaDTO1).isNotEqualTo(nextOrderThetaDTO2);
        nextOrderThetaDTO2.setId(nextOrderThetaDTO1.getId());
        assertThat(nextOrderThetaDTO1).isEqualTo(nextOrderThetaDTO2);
        nextOrderThetaDTO2.setId(2L);
        assertThat(nextOrderThetaDTO1).isNotEqualTo(nextOrderThetaDTO2);
        nextOrderThetaDTO1.setId(null);
        assertThat(nextOrderThetaDTO1).isNotEqualTo(nextOrderThetaDTO2);
    }
}
