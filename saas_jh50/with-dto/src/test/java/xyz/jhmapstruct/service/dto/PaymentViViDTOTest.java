package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentViViDTO.class);
        PaymentViViDTO paymentViViDTO1 = new PaymentViViDTO();
        paymentViViDTO1.setId(1L);
        PaymentViViDTO paymentViViDTO2 = new PaymentViViDTO();
        assertThat(paymentViViDTO1).isNotEqualTo(paymentViViDTO2);
        paymentViViDTO2.setId(paymentViViDTO1.getId());
        assertThat(paymentViViDTO1).isEqualTo(paymentViViDTO2);
        paymentViViDTO2.setId(2L);
        assertThat(paymentViViDTO1).isNotEqualTo(paymentViViDTO2);
        paymentViViDTO1.setId(null);
        assertThat(paymentViViDTO1).isNotEqualTo(paymentViViDTO2);
    }
}
