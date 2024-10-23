package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class PaymentViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentViVi.class);
        PaymentViVi paymentViVi1 = getPaymentViViSample1();
        PaymentViVi paymentViVi2 = new PaymentViVi();
        assertThat(paymentViVi1).isNotEqualTo(paymentViVi2);

        paymentViVi2.setId(paymentViVi1.getId());
        assertThat(paymentViVi1).isEqualTo(paymentViVi2);

        paymentViVi2 = getPaymentViViSample2();
        assertThat(paymentViVi1).isNotEqualTo(paymentViVi2);
    }

    @Test
    void tenantTest() {
        PaymentViVi paymentViVi = getPaymentViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        paymentViVi.setTenant(masterTenantBack);
        assertThat(paymentViVi.getTenant()).isEqualTo(masterTenantBack);

        paymentViVi.tenant(null);
        assertThat(paymentViVi.getTenant()).isNull();
    }
}
