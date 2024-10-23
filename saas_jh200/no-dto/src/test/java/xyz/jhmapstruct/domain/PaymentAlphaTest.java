package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentAlpha.class);
        PaymentAlpha paymentAlpha1 = getPaymentAlphaSample1();
        PaymentAlpha paymentAlpha2 = new PaymentAlpha();
        assertThat(paymentAlpha1).isNotEqualTo(paymentAlpha2);

        paymentAlpha2.setId(paymentAlpha1.getId());
        assertThat(paymentAlpha1).isEqualTo(paymentAlpha2);

        paymentAlpha2 = getPaymentAlphaSample2();
        assertThat(paymentAlpha1).isNotEqualTo(paymentAlpha2);
    }

    @Test
    void tenantTest() {
        PaymentAlpha paymentAlpha = getPaymentAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        paymentAlpha.setTenant(masterTenantBack);
        assertThat(paymentAlpha.getTenant()).isEqualTo(masterTenantBack);

        paymentAlpha.tenant(null);
        assertThat(paymentAlpha.getTenant()).isNull();
    }
}
