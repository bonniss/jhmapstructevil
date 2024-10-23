package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentAlpha.class);
        NextShipmentAlpha nextShipmentAlpha1 = getNextShipmentAlphaSample1();
        NextShipmentAlpha nextShipmentAlpha2 = new NextShipmentAlpha();
        assertThat(nextShipmentAlpha1).isNotEqualTo(nextShipmentAlpha2);

        nextShipmentAlpha2.setId(nextShipmentAlpha1.getId());
        assertThat(nextShipmentAlpha1).isEqualTo(nextShipmentAlpha2);

        nextShipmentAlpha2 = getNextShipmentAlphaSample2();
        assertThat(nextShipmentAlpha1).isNotEqualTo(nextShipmentAlpha2);
    }

    @Test
    void tenantTest() {
        NextShipmentAlpha nextShipmentAlpha = getNextShipmentAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentAlpha.setTenant(masterTenantBack);
        assertThat(nextShipmentAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentAlpha.tenant(null);
        assertThat(nextShipmentAlpha.getTenant()).isNull();
    }
}
