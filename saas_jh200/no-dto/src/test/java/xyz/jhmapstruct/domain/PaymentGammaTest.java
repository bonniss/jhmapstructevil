package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentGamma.class);
        PaymentGamma paymentGamma1 = getPaymentGammaSample1();
        PaymentGamma paymentGamma2 = new PaymentGamma();
        assertThat(paymentGamma1).isNotEqualTo(paymentGamma2);

        paymentGamma2.setId(paymentGamma1.getId());
        assertThat(paymentGamma1).isEqualTo(paymentGamma2);

        paymentGamma2 = getPaymentGammaSample2();
        assertThat(paymentGamma1).isNotEqualTo(paymentGamma2);
    }

    @Test
    void tenantTest() {
        PaymentGamma paymentGamma = getPaymentGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        paymentGamma.setTenant(masterTenantBack);
        assertThat(paymentGamma.getTenant()).isEqualTo(masterTenantBack);

        paymentGamma.tenant(null);
        assertThat(paymentGamma.getTenant()).isNull();
    }
}
