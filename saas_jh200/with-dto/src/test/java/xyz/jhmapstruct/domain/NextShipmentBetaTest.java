package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentBeta.class);
        NextShipmentBeta nextShipmentBeta1 = getNextShipmentBetaSample1();
        NextShipmentBeta nextShipmentBeta2 = new NextShipmentBeta();
        assertThat(nextShipmentBeta1).isNotEqualTo(nextShipmentBeta2);

        nextShipmentBeta2.setId(nextShipmentBeta1.getId());
        assertThat(nextShipmentBeta1).isEqualTo(nextShipmentBeta2);

        nextShipmentBeta2 = getNextShipmentBetaSample2();
        assertThat(nextShipmentBeta1).isNotEqualTo(nextShipmentBeta2);
    }

    @Test
    void tenantTest() {
        NextShipmentBeta nextShipmentBeta = getNextShipmentBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentBeta.setTenant(masterTenantBack);
        assertThat(nextShipmentBeta.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentBeta.tenant(null);
        assertThat(nextShipmentBeta.getTenant()).isNull();
    }
}
