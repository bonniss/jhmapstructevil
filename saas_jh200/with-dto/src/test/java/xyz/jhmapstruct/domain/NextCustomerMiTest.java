package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerMiTestSamples.*;
import static xyz.jhmapstruct.domain.OrderMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerMi.class);
        NextCustomerMi nextCustomerMi1 = getNextCustomerMiSample1();
        NextCustomerMi nextCustomerMi2 = new NextCustomerMi();
        assertThat(nextCustomerMi1).isNotEqualTo(nextCustomerMi2);

        nextCustomerMi2.setId(nextCustomerMi1.getId());
        assertThat(nextCustomerMi1).isEqualTo(nextCustomerMi2);

        nextCustomerMi2 = getNextCustomerMiSample2();
        assertThat(nextCustomerMi1).isNotEqualTo(nextCustomerMi2);
    }

    @Test
    void ordersTest() {
        NextCustomerMi nextCustomerMi = getNextCustomerMiRandomSampleGenerator();
        OrderMi orderMiBack = getOrderMiRandomSampleGenerator();

        nextCustomerMi.addOrders(orderMiBack);
        assertThat(nextCustomerMi.getOrders()).containsOnly(orderMiBack);
        assertThat(orderMiBack.getCustomer()).isEqualTo(nextCustomerMi);

        nextCustomerMi.removeOrders(orderMiBack);
        assertThat(nextCustomerMi.getOrders()).doesNotContain(orderMiBack);
        assertThat(orderMiBack.getCustomer()).isNull();

        nextCustomerMi.orders(new HashSet<>(Set.of(orderMiBack)));
        assertThat(nextCustomerMi.getOrders()).containsOnly(orderMiBack);
        assertThat(orderMiBack.getCustomer()).isEqualTo(nextCustomerMi);

        nextCustomerMi.setOrders(new HashSet<>());
        assertThat(nextCustomerMi.getOrders()).doesNotContain(orderMiBack);
        assertThat(orderMiBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerMi nextCustomerMi = getNextCustomerMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerMi.setTenant(masterTenantBack);
        assertThat(nextCustomerMi.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerMi.tenant(null);
        assertThat(nextCustomerMi.getTenant()).isNull();
    }
}
