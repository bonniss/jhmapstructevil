package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipmentMi.class);
        NextShipmentMi nextShipmentMi1 = getNextShipmentMiSample1();
        NextShipmentMi nextShipmentMi2 = new NextShipmentMi();
        assertThat(nextShipmentMi1).isNotEqualTo(nextShipmentMi2);

        nextShipmentMi2.setId(nextShipmentMi1.getId());
        assertThat(nextShipmentMi1).isEqualTo(nextShipmentMi2);

        nextShipmentMi2 = getNextShipmentMiSample2();
        assertThat(nextShipmentMi1).isNotEqualTo(nextShipmentMi2);
    }

    @Test
    void tenantTest() {
        NextShipmentMi nextShipmentMi = getNextShipmentMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipmentMi.setTenant(masterTenantBack);
        assertThat(nextShipmentMi.getTenant()).isEqualTo(masterTenantBack);

        nextShipmentMi.tenant(null);
        assertThat(nextShipmentMi.getTenant()).isNull();
    }
}
