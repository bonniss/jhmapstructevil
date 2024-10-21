package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMiMiDTO.class);
        PaymentMiMiDTO paymentMiMiDTO1 = new PaymentMiMiDTO();
        paymentMiMiDTO1.setId(1L);
        PaymentMiMiDTO paymentMiMiDTO2 = new PaymentMiMiDTO();
        assertThat(paymentMiMiDTO1).isNotEqualTo(paymentMiMiDTO2);
        paymentMiMiDTO2.setId(paymentMiMiDTO1.getId());
        assertThat(paymentMiMiDTO1).isEqualTo(paymentMiMiDTO2);
        paymentMiMiDTO2.setId(2L);
        assertThat(paymentMiMiDTO1).isNotEqualTo(paymentMiMiDTO2);
        paymentMiMiDTO1.setId(null);
        assertThat(paymentMiMiDTO1).isNotEqualTo(paymentMiMiDTO2);
    }
}
