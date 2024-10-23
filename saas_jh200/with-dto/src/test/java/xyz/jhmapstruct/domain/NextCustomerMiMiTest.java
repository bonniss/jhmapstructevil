package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderMiMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerMiMi.class);
        NextCustomerMiMi nextCustomerMiMi1 = getNextCustomerMiMiSample1();
        NextCustomerMiMi nextCustomerMiMi2 = new NextCustomerMiMi();
        assertThat(nextCustomerMiMi1).isNotEqualTo(nextCustomerMiMi2);

        nextCustomerMiMi2.setId(nextCustomerMiMi1.getId());
        assertThat(nextCustomerMiMi1).isEqualTo(nextCustomerMiMi2);

        nextCustomerMiMi2 = getNextCustomerMiMiSample2();
        assertThat(nextCustomerMiMi1).isNotEqualTo(nextCustomerMiMi2);
    }

    @Test
    void ordersTest() {
        NextCustomerMiMi nextCustomerMiMi = getNextCustomerMiMiRandomSampleGenerator();
        NextOrderMiMi nextOrderMiMiBack = getNextOrderMiMiRandomSampleGenerator();

        nextCustomerMiMi.addOrders(nextOrderMiMiBack);
        assertThat(nextCustomerMiMi.getOrders()).containsOnly(nextOrderMiMiBack);
        assertThat(nextOrderMiMiBack.getCustomer()).isEqualTo(nextCustomerMiMi);

        nextCustomerMiMi.removeOrders(nextOrderMiMiBack);
        assertThat(nextCustomerMiMi.getOrders()).doesNotContain(nextOrderMiMiBack);
        assertThat(nextOrderMiMiBack.getCustomer()).isNull();

        nextCustomerMiMi.orders(new HashSet<>(Set.of(nextOrderMiMiBack)));
        assertThat(nextCustomerMiMi.getOrders()).containsOnly(nextOrderMiMiBack);
        assertThat(nextOrderMiMiBack.getCustomer()).isEqualTo(nextCustomerMiMi);

        nextCustomerMiMi.setOrders(new HashSet<>());
        assertThat(nextCustomerMiMi.getOrders()).doesNotContain(nextOrderMiMiBack);
        assertThat(nextOrderMiMiBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerMiMi nextCustomerMiMi = getNextCustomerMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerMiMi.setTenant(masterTenantBack);
        assertThat(nextCustomerMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerMiMi.tenant(null);
        assertThat(nextCustomerMiMi.getTenant()).isNull();
    }
}
