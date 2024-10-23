package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentBetaDTO.class);
        PaymentBetaDTO paymentBetaDTO1 = new PaymentBetaDTO();
        paymentBetaDTO1.setId(1L);
        PaymentBetaDTO paymentBetaDTO2 = new PaymentBetaDTO();
        assertThat(paymentBetaDTO1).isNotEqualTo(paymentBetaDTO2);
        paymentBetaDTO2.setId(paymentBetaDTO1.getId());
        assertThat(paymentBetaDTO1).isEqualTo(paymentBetaDTO2);
        paymentBetaDTO2.setId(2L);
        assertThat(paymentBetaDTO1).isNotEqualTo(paymentBetaDTO2);
        paymentBetaDTO1.setId(null);
        assertThat(paymentBetaDTO1).isNotEqualTo(paymentBetaDTO2);
    }
}
