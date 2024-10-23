package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentGammaDTO.class);
        PaymentGammaDTO paymentGammaDTO1 = new PaymentGammaDTO();
        paymentGammaDTO1.setId(1L);
        PaymentGammaDTO paymentGammaDTO2 = new PaymentGammaDTO();
        assertThat(paymentGammaDTO1).isNotEqualTo(paymentGammaDTO2);
        paymentGammaDTO2.setId(paymentGammaDTO1.getId());
        assertThat(paymentGammaDTO1).isEqualTo(paymentGammaDTO2);
        paymentGammaDTO2.setId(2L);
        assertThat(paymentGammaDTO1).isNotEqualTo(paymentGammaDTO2);
        paymentGammaDTO1.setId(null);
        assertThat(paymentGammaDTO1).isNotEqualTo(paymentGammaDTO2);
    }
}
