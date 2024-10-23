package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentViVi.class);
        NextPaymentViVi nextPaymentViVi1 = getNextPaymentViViSample1();
        NextPaymentViVi nextPaymentViVi2 = new NextPaymentViVi();
        assertThat(nextPaymentViVi1).isNotEqualTo(nextPaymentViVi2);

        nextPaymentViVi2.setId(nextPaymentViVi1.getId());
        assertThat(nextPaymentViVi1).isEqualTo(nextPaymentViVi2);

        nextPaymentViVi2 = getNextPaymentViViSample2();
        assertThat(nextPaymentViVi1).isNotEqualTo(nextPaymentViVi2);
    }

    @Test
    void tenantTest() {
        NextPaymentViVi nextPaymentViVi = getNextPaymentViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentViVi.setTenant(masterTenantBack);
        assertThat(nextPaymentViVi.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentViVi.tenant(null);
        assertThat(nextPaymentViVi.getTenant()).isNull();
    }
}
