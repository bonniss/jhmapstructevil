package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductThetaDTO.class);
        NextProductThetaDTO nextProductThetaDTO1 = new NextProductThetaDTO();
        nextProductThetaDTO1.setId(1L);
        NextProductThetaDTO nextProductThetaDTO2 = new NextProductThetaDTO();
        assertThat(nextProductThetaDTO1).isNotEqualTo(nextProductThetaDTO2);
        nextProductThetaDTO2.setId(nextProductThetaDTO1.getId());
        assertThat(nextProductThetaDTO1).isEqualTo(nextProductThetaDTO2);
        nextProductThetaDTO2.setId(2L);
        assertThat(nextProductThetaDTO1).isNotEqualTo(nextProductThetaDTO2);
        nextProductThetaDTO1.setId(null);
        assertThat(nextProductThetaDTO1).isNotEqualTo(nextProductThetaDTO2);
    }
}
