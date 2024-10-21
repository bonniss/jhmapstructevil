package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerVi.class);
        CustomerVi customerVi1 = getCustomerViSample1();
        CustomerVi customerVi2 = new CustomerVi();
        assertThat(customerVi1).isNotEqualTo(customerVi2);

        customerVi2.setId(customerVi1.getId());
        assertThat(customerVi1).isEqualTo(customerVi2);

        customerVi2 = getCustomerViSample2();
        assertThat(customerVi1).isNotEqualTo(customerVi2);
    }

    @Test
    void ordersTest() {
        CustomerVi customerVi = getCustomerViRandomSampleGenerator();
        OrderVi orderViBack = getOrderViRandomSampleGenerator();

        customerVi.addOrders(orderViBack);
        assertThat(customerVi.getOrders()).containsOnly(orderViBack);
        assertThat(orderViBack.getCustomer()).isEqualTo(customerVi);

        customerVi.removeOrders(orderViBack);
        assertThat(customerVi.getOrders()).doesNotContain(orderViBack);
        assertThat(orderViBack.getCustomer()).isNull();

        customerVi.orders(new HashSet<>(Set.of(orderViBack)));
        assertThat(customerVi.getOrders()).containsOnly(orderViBack);
        assertThat(orderViBack.getCustomer()).isEqualTo(customerVi);

        customerVi.setOrders(new HashSet<>());
        assertThat(customerVi.getOrders()).doesNotContain(orderViBack);
        assertThat(orderViBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        CustomerVi customerVi = getCustomerViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customerVi.setTenant(masterTenantBack);
        assertThat(customerVi.getTenant()).isEqualTo(masterTenantBack);

        customerVi.tenant(null);
        assertThat(customerVi.getTenant()).isNull();
    }
}
