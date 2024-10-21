package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.OrderMiMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerMiMi.class);
        CustomerMiMi customerMiMi1 = getCustomerMiMiSample1();
        CustomerMiMi customerMiMi2 = new CustomerMiMi();
        assertThat(customerMiMi1).isNotEqualTo(customerMiMi2);

        customerMiMi2.setId(customerMiMi1.getId());
        assertThat(customerMiMi1).isEqualTo(customerMiMi2);

        customerMiMi2 = getCustomerMiMiSample2();
        assertThat(customerMiMi1).isNotEqualTo(customerMiMi2);
    }

    @Test
    void ordersTest() {
        CustomerMiMi customerMiMi = getCustomerMiMiRandomSampleGenerator();
        OrderMiMi orderMiMiBack = getOrderMiMiRandomSampleGenerator();

        customerMiMi.addOrders(orderMiMiBack);
        assertThat(customerMiMi.getOrders()).containsOnly(orderMiMiBack);
        assertThat(orderMiMiBack.getCustomer()).isEqualTo(customerMiMi);

        customerMiMi.removeOrders(orderMiMiBack);
        assertThat(customerMiMi.getOrders()).doesNotContain(orderMiMiBack);
        assertThat(orderMiMiBack.getCustomer()).isNull();

        customerMiMi.orders(new HashSet<>(Set.of(orderMiMiBack)));
        assertThat(customerMiMi.getOrders()).containsOnly(orderMiMiBack);
        assertThat(orderMiMiBack.getCustomer()).isEqualTo(customerMiMi);

        customerMiMi.setOrders(new HashSet<>());
        assertThat(customerMiMi.getOrders()).doesNotContain(orderMiMiBack);
        assertThat(orderMiMiBack.getCustomer()).isNull();
    }
}
