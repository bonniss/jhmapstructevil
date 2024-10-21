package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.PaymentViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentVi.class);
        PaymentVi paymentVi1 = getPaymentViSample1();
        PaymentVi paymentVi2 = new PaymentVi();
        assertThat(paymentVi1).isNotEqualTo(paymentVi2);

        paymentVi2.setId(paymentVi1.getId());
        assertThat(paymentVi1).isEqualTo(paymentVi2);

        paymentVi2 = getPaymentViSample2();
        assertThat(paymentVi1).isNotEqualTo(paymentVi2);
    }
}
