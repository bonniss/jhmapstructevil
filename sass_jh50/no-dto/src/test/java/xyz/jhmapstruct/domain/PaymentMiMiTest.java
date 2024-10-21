package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentMiMi.class);
        PaymentMiMi paymentMiMi1 = getPaymentMiMiSample1();
        PaymentMiMi paymentMiMi2 = new PaymentMiMi();
        assertThat(paymentMiMi1).isNotEqualTo(paymentMiMi2);

        paymentMiMi2.setId(paymentMiMi1.getId());
        assertThat(paymentMiMi1).isEqualTo(paymentMiMi2);

        paymentMiMi2 = getPaymentMiMiSample2();
        assertThat(paymentMiMi1).isNotEqualTo(paymentMiMi2);
    }

    @Test
    void tenantTest() {
        PaymentMiMi paymentMiMi = getPaymentMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        paymentMiMi.setTenant(masterTenantBack);
        assertThat(paymentMiMi.getTenant()).isEqualTo(masterTenantBack);

        paymentMiMi.tenant(null);
        assertThat(paymentMiMi.getTenant()).isNull();
    }
}
