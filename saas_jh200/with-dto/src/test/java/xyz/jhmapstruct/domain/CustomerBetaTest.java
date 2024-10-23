package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerBetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderBetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerBeta.class);
        CustomerBeta customerBeta1 = getCustomerBetaSample1();
        CustomerBeta customerBeta2 = new CustomerBeta();
        assertThat(customerBeta1).isNotEqualTo(customerBeta2);

        customerBeta2.setId(customerBeta1.getId());
        assertThat(customerBeta1).isEqualTo(customerBeta2);

        customerBeta2 = getCustomerBetaSample2();
        assertThat(customerBeta1).isNotEqualTo(customerBeta2);
    }

    @Test
    void ordersTest() {
        CustomerBeta customerBeta = getCustomerBetaRandomSampleGenerator();
        OrderBeta orderBetaBack = getOrderBetaRandomSampleGenerator();

        customerBeta.addOrders(orderBetaBack);
        assertThat(customerBeta.getOrders()).containsOnly(orderBetaBack);
        assertThat(orderBetaBack.getCustomer()).isEqualTo(customerBeta);

        customerBeta.removeOrders(orderBetaBack);
        assertThat(customerBeta.getOrders()).doesNotContain(orderBetaBack);
        assertThat(orderBetaBack.getCustomer()).isNull();

        customerBeta.orders(new HashSet<>(Set.of(orderBetaBack)));
        assertThat(customerBeta.getOrders()).containsOnly(orderBetaBack);
        assertThat(orderBetaBack.getCustomer()).isEqualTo(customerBeta);

        customerBeta.setOrders(new HashSet<>());
        assertThat(customerBeta.getOrders()).doesNotContain(orderBetaBack);
        assertThat(orderBetaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        CustomerBeta customerBeta = getCustomerBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customerBeta.setTenant(masterTenantBack);
        assertThat(customerBeta.getTenant()).isEqualTo(masterTenantBack);

        customerBeta.tenant(null);
        assertThat(customerBeta.getTenant()).isNull();
    }
}
