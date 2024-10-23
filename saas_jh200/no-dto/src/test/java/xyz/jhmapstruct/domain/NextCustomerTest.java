package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomer.class);
        NextCustomer nextCustomer1 = getNextCustomerSample1();
        NextCustomer nextCustomer2 = new NextCustomer();
        assertThat(nextCustomer1).isNotEqualTo(nextCustomer2);

        nextCustomer2.setId(nextCustomer1.getId());
        assertThat(nextCustomer1).isEqualTo(nextCustomer2);

        nextCustomer2 = getNextCustomerSample2();
        assertThat(nextCustomer1).isNotEqualTo(nextCustomer2);
    }

    @Test
    void ordersTest() {
        NextCustomer nextCustomer = getNextCustomerRandomSampleGenerator();
        NextOrder nextOrderBack = getNextOrderRandomSampleGenerator();

        nextCustomer.addOrders(nextOrderBack);
        assertThat(nextCustomer.getOrders()).containsOnly(nextOrderBack);
        assertThat(nextOrderBack.getCustomer()).isEqualTo(nextCustomer);

        nextCustomer.removeOrders(nextOrderBack);
        assertThat(nextCustomer.getOrders()).doesNotContain(nextOrderBack);
        assertThat(nextOrderBack.getCustomer()).isNull();

        nextCustomer.orders(new HashSet<>(Set.of(nextOrderBack)));
        assertThat(nextCustomer.getOrders()).containsOnly(nextOrderBack);
        assertThat(nextOrderBack.getCustomer()).isEqualTo(nextCustomer);

        nextCustomer.setOrders(new HashSet<>());
        assertThat(nextCustomer.getOrders()).doesNotContain(nextOrderBack);
        assertThat(nextOrderBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomer nextCustomer = getNextCustomerRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomer.setTenant(masterTenantBack);
        assertThat(nextCustomer.getTenant()).isEqualTo(masterTenantBack);

        nextCustomer.tenant(null);
        assertThat(nextCustomer.getTenant()).isNull();
    }
}
