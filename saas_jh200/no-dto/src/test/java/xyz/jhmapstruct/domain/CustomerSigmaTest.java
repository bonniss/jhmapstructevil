package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderSigmaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerSigma.class);
        CustomerSigma customerSigma1 = getCustomerSigmaSample1();
        CustomerSigma customerSigma2 = new CustomerSigma();
        assertThat(customerSigma1).isNotEqualTo(customerSigma2);

        customerSigma2.setId(customerSigma1.getId());
        assertThat(customerSigma1).isEqualTo(customerSigma2);

        customerSigma2 = getCustomerSigmaSample2();
        assertThat(customerSigma1).isNotEqualTo(customerSigma2);
    }

    @Test
    void ordersTest() {
        CustomerSigma customerSigma = getCustomerSigmaRandomSampleGenerator();
        OrderSigma orderSigmaBack = getOrderSigmaRandomSampleGenerator();

        customerSigma.addOrders(orderSigmaBack);
        assertThat(customerSigma.getOrders()).containsOnly(orderSigmaBack);
        assertThat(orderSigmaBack.getCustomer()).isEqualTo(customerSigma);

        customerSigma.removeOrders(orderSigmaBack);
        assertThat(customerSigma.getOrders()).doesNotContain(orderSigmaBack);
        assertThat(orderSigmaBack.getCustomer()).isNull();

        customerSigma.orders(new HashSet<>(Set.of(orderSigmaBack)));
        assertThat(customerSigma.getOrders()).containsOnly(orderSigmaBack);
        assertThat(orderSigmaBack.getCustomer()).isEqualTo(customerSigma);

        customerSigma.setOrders(new HashSet<>());
        assertThat(customerSigma.getOrders()).doesNotContain(orderSigmaBack);
        assertThat(orderSigmaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        CustomerSigma customerSigma = getCustomerSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customerSigma.setTenant(masterTenantBack);
        assertThat(customerSigma.getTenant()).isEqualTo(masterTenantBack);

        customerSigma.tenant(null);
        assertThat(customerSigma.getTenant()).isNull();
    }
}
