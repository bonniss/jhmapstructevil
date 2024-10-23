package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTheta.class);
        PaymentTheta paymentTheta1 = getPaymentThetaSample1();
        PaymentTheta paymentTheta2 = new PaymentTheta();
        assertThat(paymentTheta1).isNotEqualTo(paymentTheta2);

        paymentTheta2.setId(paymentTheta1.getId());
        assertThat(paymentTheta1).isEqualTo(paymentTheta2);

        paymentTheta2 = getPaymentThetaSample2();
        assertThat(paymentTheta1).isNotEqualTo(paymentTheta2);
    }

    @Test
    void tenantTest() {
        PaymentTheta paymentTheta = getPaymentThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        paymentTheta.setTenant(masterTenantBack);
        assertThat(paymentTheta.getTenant()).isEqualTo(masterTenantBack);

        paymentTheta.tenant(null);
        assertThat(paymentTheta.getTenant()).isNull();
    }
}
