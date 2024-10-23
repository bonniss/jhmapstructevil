package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentMi.class);
        NextPaymentMi nextPaymentMi1 = getNextPaymentMiSample1();
        NextPaymentMi nextPaymentMi2 = new NextPaymentMi();
        assertThat(nextPaymentMi1).isNotEqualTo(nextPaymentMi2);

        nextPaymentMi2.setId(nextPaymentMi1.getId());
        assertThat(nextPaymentMi1).isEqualTo(nextPaymentMi2);

        nextPaymentMi2 = getNextPaymentMiSample2();
        assertThat(nextPaymentMi1).isNotEqualTo(nextPaymentMi2);
    }

    @Test
    void tenantTest() {
        NextPaymentMi nextPaymentMi = getNextPaymentMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentMi.setTenant(masterTenantBack);
        assertThat(nextPaymentMi.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentMi.tenant(null);
        assertThat(nextPaymentMi.getTenant()).isNull();
    }
}
