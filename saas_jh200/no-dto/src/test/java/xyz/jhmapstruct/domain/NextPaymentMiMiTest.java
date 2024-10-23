package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentMiMi.class);
        NextPaymentMiMi nextPaymentMiMi1 = getNextPaymentMiMiSample1();
        NextPaymentMiMi nextPaymentMiMi2 = new NextPaymentMiMi();
        assertThat(nextPaymentMiMi1).isNotEqualTo(nextPaymentMiMi2);

        nextPaymentMiMi2.setId(nextPaymentMiMi1.getId());
        assertThat(nextPaymentMiMi1).isEqualTo(nextPaymentMiMi2);

        nextPaymentMiMi2 = getNextPaymentMiMiSample2();
        assertThat(nextPaymentMiMi1).isNotEqualTo(nextPaymentMiMi2);
    }

    @Test
    void tenantTest() {
        NextPaymentMiMi nextPaymentMiMi = getNextPaymentMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentMiMi.setTenant(masterTenantBack);
        assertThat(nextPaymentMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentMiMi.tenant(null);
        assertThat(nextPaymentMiMi.getTenant()).isNull();
    }
}
