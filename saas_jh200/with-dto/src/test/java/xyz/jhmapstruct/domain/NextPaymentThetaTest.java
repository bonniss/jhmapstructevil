package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentTheta.class);
        NextPaymentTheta nextPaymentTheta1 = getNextPaymentThetaSample1();
        NextPaymentTheta nextPaymentTheta2 = new NextPaymentTheta();
        assertThat(nextPaymentTheta1).isNotEqualTo(nextPaymentTheta2);

        nextPaymentTheta2.setId(nextPaymentTheta1.getId());
        assertThat(nextPaymentTheta1).isEqualTo(nextPaymentTheta2);

        nextPaymentTheta2 = getNextPaymentThetaSample2();
        assertThat(nextPaymentTheta1).isNotEqualTo(nextPaymentTheta2);
    }

    @Test
    void tenantTest() {
        NextPaymentTheta nextPaymentTheta = getNextPaymentThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextPaymentTheta.setTenant(masterTenantBack);
        assertThat(nextPaymentTheta.getTenant()).isEqualTo(masterTenantBack);

        nextPaymentTheta.tenant(null);
        assertThat(nextPaymentTheta.getTenant()).isNull();
    }
}
