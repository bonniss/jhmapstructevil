package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerMiTestSamples.*;
import static xyz.jhmapstruct.domain.OrderMiTestSamples.*;

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
        OrderMi orderMiBack = getOrderMiRandomSampleGenerator();

        customerMi.addOrders(orderMiBack);
        assertThat(customerMi.getOrders()).containsOnly(orderMiBack);
        assertThat(orderMiBack.getCustomer()).isEqualTo(customerMi);

        customerMi.removeOrders(orderMiBack);
        assertThat(customerMi.getOrders()).doesNotContain(orderMiBack);
        assertThat(orderMiBack.getCustomer()).isNull();

        customerMi.orders(new HashSet<>(Set.of(orderMiBack)));
        assertThat(customerMi.getOrders()).containsOnly(orderMiBack);
        assertThat(orderMiBack.getCustomer()).isEqualTo(customerMi);

        customerMi.setOrders(new HashSet<>());
        assertThat(customerMi.getOrders()).doesNotContain(orderMiBack);
        assertThat(orderMiBack.getCustomer()).isNull();
    }
}
