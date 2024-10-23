package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentSigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentSigma.class);
        NextShipmentSigma nextShipmentSigma1 = getNextShipmentSigmaSample1();
        NextShipmentSigma nextShipmentSigma2 = new NextShipmentSigma();
        assertThat(nextShipmentSigma1).isNotEqualTo(nextShipmentSigma2);

        nextShipmentSigma2.setId(nextShipmentSigma1.getId());
        assertThat(nextShipmentSigma1).isEqualTo(nextShipmentSigma2);

        nextShipmentSigma2 = getNextShipmentSigmaSample2();
        assertThat(nextShipmentSigma1).isNotEqualTo(nextShipmentSigma2);
    }

    @Test
    void tenantTest() {
        NextShipmentSigma nextShipmentSigma = getNextShipmentSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentSigma.setTenant(masterTenantBack);
        assertThat(nextShipmentSigma.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentSigma.tenant(null);
        assertThat(nextShipmentSigma.getTenant()).isNull();
    }
}
