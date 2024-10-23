package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentSigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentSigma.class);
        PaymentSigma paymentSigma1 = getPaymentSigmaSample1();
        PaymentSigma paymentSigma2 = new PaymentSigma();
        assertThat(paymentSigma1).isNotEqualTo(paymentSigma2);

        paymentSigma2.setId(paymentSigma1.getId());
        assertThat(paymentSigma1).isEqualTo(paymentSigma2);

        paymentSigma2 = getPaymentSigmaSample2();
        assertThat(paymentSigma1).isNotEqualTo(paymentSigma2);
    }

    @Test
    void tenantTest() {
        PaymentSigma paymentSigma = getPaymentSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        paymentSigma.setTenant(masterTenantBack);
        assertThat(paymentSigma.getTenant()).isEqualTo(masterTenantBack);

        paymentSigma.tenant(null);
        assertThat(paymentSigma.getTenant()).isNull();
    }
}
