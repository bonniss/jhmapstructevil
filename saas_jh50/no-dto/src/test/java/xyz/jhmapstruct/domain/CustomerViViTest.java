package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerViViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderViViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerViVi.class);
        CustomerViVi customerViVi1 = getCustomerViViSample1();
        CustomerViVi customerViVi2 = new CustomerViVi();
        assertThat(customerViVi1).isNotEqualTo(customerViVi2);

        customerViVi2.setId(customerViVi1.getId());
        assertThat(customerViVi1).isEqualTo(customerViVi2);

        customerViVi2 = getCustomerViViSample2();
        assertThat(customerViVi1).isNotEqualTo(customerViVi2);
    }

    @Test
    void ordersTest() {
        CustomerViVi customerViVi = getCustomerViViRandomSampleGenerator();
        OrderViVi orderViViBack = getOrderViViRandomSampleGenerator();

        customerViVi.addOrders(orderViViBack);
        assertThat(customerViVi.getOrders()).containsOnly(orderViViBack);
        assertThat(orderViViBack.getCustomer()).isEqualTo(customerViVi);

        customerViVi.removeOrders(orderViViBack);
        assertThat(customerViVi.getOrders()).doesNotContain(orderViViBack);
        assertThat(orderViViBack.getCustomer()).isNull();

        customerViVi.orders(new HashSet<>(Set.of(orderViViBack)));
        assertThat(customerViVi.getOrders()).containsOnly(orderViViBack);
        assertThat(orderViViBack.getCustomer()).isEqualTo(customerViVi);

        customerViVi.setOrders(new HashSet<>());
        assertThat(customerViVi.getOrders()).doesNotContain(orderViViBack);
        assertThat(orderViViBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        CustomerViVi customerViVi = getCustomerViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customerViVi.setTenant(masterTenantBack);
        assertThat(customerViVi.getTenant()).isEqualTo(masterTenantBack);

        customerViVi.tenant(null);
        assertThat(customerViVi.getTenant()).isNull();
    }
}
