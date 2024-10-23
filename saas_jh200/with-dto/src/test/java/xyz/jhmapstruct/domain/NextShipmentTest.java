package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextShipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextShipment.class);
        NextShipment nextShipment1 = getNextShipmentSample1();
        NextShipment nextShipment2 = new NextShipment();
        assertThat(nextShipment1).isNotEqualTo(nextShipment2);

        nextShipment2.setId(nextShipment1.getId());
        assertThat(nextShipment1).isEqualTo(nextShipment2);

        nextShipment2 = getNextShipmentSample2();
        assertThat(nextShipment1).isNotEqualTo(nextShipment2);
    }

    @Test
    void tenantTest() {
        NextShipment nextShipment = getNextShipmentRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextShipment.setTenant(masterTenantBack);
        assertThat(nextShipment.getTenant()).isEqualTo(masterTenantBack);

        nextShipment.tenant(null);
        assertThat(nextShipment.getTenant()).isNull();
    }
}
