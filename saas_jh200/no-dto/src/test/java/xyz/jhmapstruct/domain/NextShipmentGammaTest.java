package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentGamma.class);
        NextShipmentGamma nextShipmentGamma1 = getNextShipmentGammaSample1();
        NextShipmentGamma nextShipmentGamma2 = new NextShipmentGamma();
        assertThat(nextShipmentGamma1).isNotEqualTo(nextShipmentGamma2);

        nextShipmentGamma2.setId(nextShipmentGamma1.getId());
        assertThat(nextShipmentGamma1).isEqualTo(nextShipmentGamma2);

        nextShipmentGamma2 = getNextShipmentGammaSample2();
        assertThat(nextShipmentGamma1).isNotEqualTo(nextShipmentGamma2);
    }

    @Test
    void tenantTest() {
        NextShipmentGamma nextShipmentGamma = getNextShipmentGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentGamma.setTenant(masterTenantBack);
        assertThat(nextShipmentGamma.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentGamma.tenant(null);
        assertThat(nextShipmentGamma.getTenant()).isNull();
    }
}
