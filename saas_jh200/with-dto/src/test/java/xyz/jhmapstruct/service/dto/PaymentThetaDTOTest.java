package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentThetaDTO.class);
        PaymentThetaDTO paymentThetaDTO1 = new PaymentThetaDTO();
        paymentThetaDTO1.setId(1L);
        PaymentThetaDTO paymentThetaDTO2 = new PaymentThetaDTO();
        assertThat(paymentThetaDTO1).isNotEqualTo(paymentThetaDTO2);
        paymentThetaDTO2.setId(paymentThetaDTO1.getId());
        assertThat(paymentThetaDTO1).isEqualTo(paymentThetaDTO2);
        paymentThetaDTO2.setId(2L);
        assertThat(paymentThetaDTO1).isNotEqualTo(paymentThetaDTO2);
        paymentThetaDTO1.setId(null);
        assertThat(paymentThetaDTO1).isNotEqualTo(paymentThetaDTO2);
    }
}
