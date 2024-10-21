package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void ordersTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        customer.addOrders(orderBack);
        assertThat(customer.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(customer);

        customer.removeOrders(orderBack);
        assertThat(customer.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getCustomer()).isNull();

        customer.orders(new HashSet<>(Set.of(orderBack)));
        assertThat(customer.getOrders()).containsOnly(orderBack);
        assertThat(orderBack.getCustomer()).isEqualTo(customer);

        customer.setOrders(new HashSet<>());
        assertThat(customer.getOrders()).doesNotContain(orderBack);
        assertThat(orderBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customer.setTenant(masterTenantBack);
        assertThat(customer.getTenant()).isEqualTo(masterTenantBack);

        customer.tenant(null);
        assertThat(customer.getTenant()).isNull();
    }
}
