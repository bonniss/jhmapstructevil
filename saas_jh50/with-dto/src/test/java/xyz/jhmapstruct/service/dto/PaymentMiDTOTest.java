package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMiDTO.class);
        PaymentMiDTO paymentMiDTO1 = new PaymentMiDTO();
        paymentMiDTO1.setId(1L);
        PaymentMiDTO paymentMiDTO2 = new PaymentMiDTO();
        assertThat(paymentMiDTO1).isNotEqualTo(paymentMiDTO2);
        paymentMiDTO2.setId(paymentMiDTO1.getId());
        assertThat(paymentMiDTO1).isEqualTo(paymentMiDTO2);
        paymentMiDTO2.setId(2L);
        assertThat(paymentMiDTO1).isNotEqualTo(paymentMiDTO2);
        paymentMiDTO1.setId(null);
        assertThat(paymentMiDTO1).isNotEqualTo(paymentMiDTO2);
    }
}
