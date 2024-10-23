package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentVi.class);
        NextPaymentVi nextPaymentVi1 = getNextPaymentViSample1();
        NextPaymentVi nextPaymentVi2 = new NextPaymentVi();
        assertThat(nextPaymentVi1).isNotEqualTo(nextPaymentVi2);

        nextPaymentVi2.setId(nextPaymentVi1.getId());
        assertThat(nextPaymentVi1).isEqualTo(nextPaymentVi2);

        nextPaymentVi2 = getNextPaymentViSample2();
        assertThat(nextPaymentVi1).isNotEqualTo(nextPaymentVi2);
    }

    @Test
    void tenantTest() {
        NextPaymentVi nextPaymentVi = getNextPaymentViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentVi.setTenant(masterTenantBack);
        assertThat(nextPaymentVi.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentVi.tenant(null);
        assertThat(nextPaymentVi.getTenant()).isNull();
    }
}
