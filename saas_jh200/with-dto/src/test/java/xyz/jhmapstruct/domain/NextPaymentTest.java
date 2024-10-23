package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPayment.class);
        NextPayment nextPayment1 = getNextPaymentSample1();
        NextPayment nextPayment2 = new NextPayment();
        assertThat(nextPayment1).isNotEqualTo(nextPayment2);

        nextPayment2.setId(nextPayment1.getId());
        assertThat(nextPayment1).isEqualTo(nextPayment2);

        nextPayment2 = getNextPaymentSample2();
        assertThat(nextPayment1).isNotEqualTo(nextPayment2);
    }

    @Test
    void tenantTest() {
        NextPayment nextPayment = getNextPaymentRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPayment.setTenant(masterTenantBack);
        assertThat(nextPayment.getTenant()).isEqualTo(masterTenantBack);

        nextPayment.tenant(null);
        assertThat(nextPayment.getTenant()).isNull();
    }
}
