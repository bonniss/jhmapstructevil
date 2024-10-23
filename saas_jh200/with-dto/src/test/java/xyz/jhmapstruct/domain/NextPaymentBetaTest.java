package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentBeta.class);
        NextPaymentBeta nextPaymentBeta1 = getNextPaymentBetaSample1();
        NextPaymentBeta nextPaymentBeta2 = new NextPaymentBeta();
        assertThat(nextPaymentBeta1).isNotEqualTo(nextPaymentBeta2);

        nextPaymentBeta2.setId(nextPaymentBeta1.getId());
        assertThat(nextPaymentBeta1).isEqualTo(nextPaymentBeta2);

        nextPaymentBeta2 = getNextPaymentBetaSample2();
        assertThat(nextPaymentBeta1).isNotEqualTo(nextPaymentBeta2);
    }

    @Test
    void tenantTest() {
        NextPaymentBeta nextPaymentBeta = getNextPaymentBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentBeta.setTenant(masterTenantBack);
        assertThat(nextPaymentBeta.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentBeta.tenant(null);
        assertThat(nextPaymentBeta.getTenant()).isNull();
    }
}
