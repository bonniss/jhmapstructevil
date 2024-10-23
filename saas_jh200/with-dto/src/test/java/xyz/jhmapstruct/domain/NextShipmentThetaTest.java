package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentTheta.class);
        NextShipmentTheta nextShipmentTheta1 = getNextShipmentThetaSample1();
        NextShipmentTheta nextShipmentTheta2 = new NextShipmentTheta();
        assertThat(nextShipmentTheta1).isNotEqualTo(nextShipmentTheta2);

        nextShipmentTheta2.setId(nextShipmentTheta1.getId());
        assertThat(nextShipmentTheta1).isEqualTo(nextShipmentTheta2);

        nextShipmentTheta2 = getNextShipmentThetaSample2();
        assertThat(nextShipmentTheta1).isNotEqualTo(nextShipmentTheta2);
    }

    @Test
    void tenantTest() {
        NextShipmentTheta nextShipmentTheta = getNextShipmentThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentTheta.setTenant(masterTenantBack);
        assertThat(nextShipmentTheta.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentTheta.tenant(null);
        assertThat(nextShipmentTheta.getTenant()).isNull();
    }
}
