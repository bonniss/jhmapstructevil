package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentSigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentSigma.class);
        NextPaymentSigma nextPaymentSigma1 = getNextPaymentSigmaSample1();
        NextPaymentSigma nextPaymentSigma2 = new NextPaymentSigma();
        assertThat(nextPaymentSigma1).isNotEqualTo(nextPaymentSigma2);

        nextPaymentSigma2.setId(nextPaymentSigma1.getId());
        assertThat(nextPaymentSigma1).isEqualTo(nextPaymentSigma2);

        nextPaymentSigma2 = getNextPaymentSigmaSample2();
        assertThat(nextPaymentSigma1).isNotEqualTo(nextPaymentSigma2);
    }

    @Test
    void tenantTest() {
        NextPaymentSigma nextPaymentSigma = getNextPaymentSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentSigma.setTenant(masterTenantBack);
        assertThat(nextPaymentSigma.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentSigma.tenant(null);
        assertThat(nextPaymentSigma.getTenant()).isNull();
    }
}
