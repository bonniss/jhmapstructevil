package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentThetaDTO.class);
        NextPaymentThetaDTO nextPaymentThetaDTO1 = new NextPaymentThetaDTO();
        nextPaymentThetaDTO1.setId(1L);
        NextPaymentThetaDTO nextPaymentThetaDTO2 = new NextPaymentThetaDTO();
        assertThat(nextPaymentThetaDTO1).isNotEqualTo(nextPaymentThetaDTO2);
        nextPaymentThetaDTO2.setId(nextPaymentThetaDTO1.getId());
        assertThat(nextPaymentThetaDTO1).isEqualTo(nextPaymentThetaDTO2);
        nextPaymentThetaDTO2.setId(2L);
        assertThat(nextPaymentThetaDTO1).isNotEqualTo(nextPaymentThetaDTO2);
        nextPaymentThetaDTO1.setId(null);
        assertThat(nextPaymentThetaDTO1).isNotEqualTo(nextPaymentThetaDTO2);
    }
}
