package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerMiTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerMi.class);
        CustomerMi customerMi1 = getCustomerMiSample1();
        CustomerMi customerMi2 = new CustomerMi();
        assertThat(customerMi1).isNotEqualTo(customerMi2);

        customerMi2.setId(customerMi1.getId());
        assertThat(customerMi1).isEqualTo(customerMi2);

        customerMi2 = getCustomerMiSample2();
        assertThat(customerMi1).isNotEqualTo(customerMi2);
    }

    @Test
    void ordersTest() {
        CustomerMi customerMi = getCustomerMiRandomSampleGenerator();
        NextOrderMi nextOrderMiBack = getNextOrderMiRandomSampleGenerator();

        customerMi.addOrders(nextOrderMiBack);
        assertThat(customerMi.getOrders()).containsOnly(nextOrderMiBack);
        assertThat(nextOrderMiBack.getCustomer()).isEqualTo(customerMi);

        customerMi.removeOrders(nextOrderMiBack);
        assertThat(customerMi.getOrders()).doesNotContain(nextOrderMiBack);
        assertThat(nextOrderMiBack.getCustomer()).isNull();

        customerMi.orders(new HashSet<>(Set.of(nextOrderMiBack)));
        assertThat(customerMi.getOrders()).containsOnly(nextOrderMiBack);
        assertThat(nextOrderMiBack.getCustomer()).isEqualTo(customerMi);

        customerMi.setOrders(new HashSet<>());
        assertThat(customerMi.getOrders()).doesNotContain(nextOrderMiBack);
        assertThat(nextOrderMiBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        CustomerMi customerMi = getCustomerMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customerMi.setTenant(masterTenantBack);
        assertThat(customerMi.getTenant()).isEqualTo(masterTenantBack);

        customerMi.tenant(null);
        assertThat(customerMi.getTenant()).isNull();
    }
}
