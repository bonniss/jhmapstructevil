package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentMiMi.class);
        NextShipmentMiMi nextShipmentMiMi1 = getNextShipmentMiMiSample1();
        NextShipmentMiMi nextShipmentMiMi2 = new NextShipmentMiMi();
        assertThat(nextShipmentMiMi1).isNotEqualTo(nextShipmentMiMi2);

        nextShipmentMiMi2.setId(nextShipmentMiMi1.getId());
        assertThat(nextShipmentMiMi1).isEqualTo(nextShipmentMiMi2);

        nextShipmentMiMi2 = getNextShipmentMiMiSample2();
        assertThat(nextShipmentMiMi1).isNotEqualTo(nextShipmentMiMi2);
    }

    @Test
    void tenantTest() {
        NextShipmentMiMi nextShipmentMiMi = getNextShipmentMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentMiMi.setTenant(masterTenantBack);
        assertThat(nextShipmentMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentMiMi.tenant(null);
        assertThat(nextShipmentMiMi.getTenant()).isNull();
    }
}
