package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentAlpha.class);
        NextPaymentAlpha nextPaymentAlpha1 = getNextPaymentAlphaSample1();
        NextPaymentAlpha nextPaymentAlpha2 = new NextPaymentAlpha();
        assertThat(nextPaymentAlpha1).isNotEqualTo(nextPaymentAlpha2);

        nextPaymentAlpha2.setId(nextPaymentAlpha1.getId());
        assertThat(nextPaymentAlpha1).isEqualTo(nextPaymentAlpha2);

        nextPaymentAlpha2 = getNextPaymentAlphaSample2();
        assertThat(nextPaymentAlpha1).isNotEqualTo(nextPaymentAlpha2);
    }

    @Test
    void tenantTest() {
        NextPaymentAlpha nextPaymentAlpha = getNextPaymentAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentAlpha.setTenant(masterTenantBack);
        assertThat(nextPaymentAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentAlpha.tenant(null);
        assertThat(nextPaymentAlpha.getTenant()).isNull();
    }
}
