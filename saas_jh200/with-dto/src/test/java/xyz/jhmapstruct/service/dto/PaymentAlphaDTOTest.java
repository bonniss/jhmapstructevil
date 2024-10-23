package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentAlphaDTO.class);
        PaymentAlphaDTO paymentAlphaDTO1 = new PaymentAlphaDTO();
        paymentAlphaDTO1.setId(1L);
        PaymentAlphaDTO paymentAlphaDTO2 = new PaymentAlphaDTO();
        assertThat(paymentAlphaDTO1).isNotEqualTo(paymentAlphaDTO2);
        paymentAlphaDTO2.setId(paymentAlphaDTO1.getId());
        assertThat(paymentAlphaDTO1).isEqualTo(paymentAlphaDTO2);
        paymentAlphaDTO2.setId(2L);
        assertThat(paymentAlphaDTO1).isNotEqualTo(paymentAlphaDTO2);
        paymentAlphaDTO1.setId(null);
        assertThat(paymentAlphaDTO1).isNotEqualTo(paymentAlphaDTO2);
    }
}
