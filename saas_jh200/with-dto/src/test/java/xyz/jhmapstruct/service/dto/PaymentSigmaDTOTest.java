package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentSigmaDTO.class);
        PaymentSigmaDTO paymentSigmaDTO1 = new PaymentSigmaDTO();
        paymentSigmaDTO1.setId(1L);
        PaymentSigmaDTO paymentSigmaDTO2 = new PaymentSigmaDTO();
        assertThat(paymentSigmaDTO1).isNotEqualTo(paymentSigmaDTO2);
        paymentSigmaDTO2.setId(paymentSigmaDTO1.getId());
        assertThat(paymentSigmaDTO1).isEqualTo(paymentSigmaDTO2);
        paymentSigmaDTO2.setId(2L);
        assertThat(paymentSigmaDTO1).isNotEqualTo(paymentSigmaDTO2);
        paymentSigmaDTO1.setId(null);
        assertThat(paymentSigmaDTO1).isNotEqualTo(paymentSigmaDTO2);
    }
}
