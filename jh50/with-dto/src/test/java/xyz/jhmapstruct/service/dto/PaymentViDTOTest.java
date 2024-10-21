package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentViDTO.class);
        PaymentViDTO paymentViDTO1 = new PaymentViDTO();
        paymentViDTO1.setId(1L);
        PaymentViDTO paymentViDTO2 = new PaymentViDTO();
        assertThat(paymentViDTO1).isNotEqualTo(paymentViDTO2);
        paymentViDTO2.setId(paymentViDTO1.getId());
        assertThat(paymentViDTO1).isEqualTo(paymentViDTO2);
        paymentViDTO2.setId(2L);
        assertThat(paymentViDTO1).isNotEqualTo(paymentViDTO2);
        paymentViDTO1.setId(null);
        assertThat(paymentViDTO1).isNotEqualTo(paymentViDTO2);
    }
}
