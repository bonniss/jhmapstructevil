package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.PaymentMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMi.class);
        PaymentMi paymentMi1 = getPaymentMiSample1();
        PaymentMi paymentMi2 = new PaymentMi();
        assertThat(paymentMi1).isNotEqualTo(paymentMi2);

        paymentMi2.setId(paymentMi1.getId());
        assertThat(paymentMi1).isEqualTo(paymentMi2);

        paymentMi2 = getPaymentMiSample2();
        assertThat(paymentMi1).isNotEqualTo(paymentMi2);
    }
}
