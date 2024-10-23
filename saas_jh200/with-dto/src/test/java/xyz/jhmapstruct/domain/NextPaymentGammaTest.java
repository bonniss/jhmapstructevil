package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentGamma.class);
        NextPaymentGamma nextPaymentGamma1 = getNextPaymentGammaSample1();
        NextPaymentGamma nextPaymentGamma2 = new NextPaymentGamma();
        assertThat(nextPaymentGamma1).isNotEqualTo(nextPaymentGamma2);

        nextPaymentGamma2.setId(nextPaymentGamma1.getId());
        assertThat(nextPaymentGamma1).isEqualTo(nextPaymentGamma2);

        nextPaymentGamma2 = getNextPaymentGammaSample2();
        assertThat(nextPaymentGamma1).isNotEqualTo(nextPaymentGamma2);
    }

    @Test
    void tenantTest() {
        NextPaymentGamma nextPaymentGamma = getNextPaymentGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentGamma.setTenant(masterTenantBack);
        assertThat(nextPaymentGamma.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentGamma.tenant(null);
        assertThat(nextPaymentGamma.getTenant()).isNull();
    }
}
