package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentBeta.class);
        PaymentBeta paymentBeta1 = getPaymentBetaSample1();
        PaymentBeta paymentBeta2 = new PaymentBeta();
        assertThat(paymentBeta1).isNotEqualTo(paymentBeta2);

        paymentBeta2.setId(paymentBeta1.getId());
        assertThat(paymentBeta1).isEqualTo(paymentBeta2);

        paymentBeta2 = getPaymentBetaSample2();
        assertThat(paymentBeta1).isNotEqualTo(paymentBeta2);
    }

    @Test
    void tenantTest() {
        PaymentBeta paymentBeta = getPaymentBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        paymentBeta.setTenant(masterTenantBack);
        assertThat(paymentBeta.getTenant()).isEqualTo(masterTenantBack);

        paymentBeta.tenant(null);
        assertThat(paymentBeta.getTenant()).isNull();
    }
}
