package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderAlphaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAlpha.class);
        CustomerAlpha customerAlpha1 = getCustomerAlphaSample1();
        CustomerAlpha customerAlpha2 = new CustomerAlpha();
        assertThat(customerAlpha1).isNotEqualTo(customerAlpha2);

        customerAlpha2.setId(customerAlpha1.getId());
        assertThat(customerAlpha1).isEqualTo(customerAlpha2);

        customerAlpha2 = getCustomerAlphaSample2();
        assertThat(customerAlpha1).isNotEqualTo(customerAlpha2);
    }

    @Test
    void ordersTest() {
        CustomerAlpha customerAlpha = getCustomerAlphaRandomSampleGenerator();
        OrderAlpha orderAlphaBack = getOrderAlphaRandomSampleGenerator();

        customerAlpha.addOrders(orderAlphaBack);
        assertThat(customerAlpha.getOrders()).containsOnly(orderAlphaBack);
        assertThat(orderAlphaBack.getCustomer()).isEqualTo(customerAlpha);

        customerAlpha.removeOrders(orderAlphaBack);
        assertThat(customerAlpha.getOrders()).doesNotContain(orderAlphaBack);
        assertThat(orderAlphaBack.getCustomer()).isNull();

        customerAlpha.orders(new HashSet<>(Set.of(orderAlphaBack)));
        assertThat(customerAlpha.getOrders()).containsOnly(orderAlphaBack);
        assertThat(orderAlphaBack.getCustomer()).isEqualTo(customerAlpha);

        customerAlpha.setOrders(new HashSet<>());
        assertThat(customerAlpha.getOrders()).doesNotContain(orderAlphaBack);
        assertThat(orderAlphaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        CustomerAlpha customerAlpha = getCustomerAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customerAlpha.setTenant(masterTenantBack);
        assertThat(customerAlpha.getTenant()).isEqualTo(masterTenantBack);

        customerAlpha.tenant(null);
        assertThat(customerAlpha.getTenant()).isNull();
    }
}
