package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerGammaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderGammaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerGamma.class);
        CustomerGamma customerGamma1 = getCustomerGammaSample1();
        CustomerGamma customerGamma2 = new CustomerGamma();
        assertThat(customerGamma1).isNotEqualTo(customerGamma2);

        customerGamma2.setId(customerGamma1.getId());
        assertThat(customerGamma1).isEqualTo(customerGamma2);

        customerGamma2 = getCustomerGammaSample2();
        assertThat(customerGamma1).isNotEqualTo(customerGamma2);
    }

    @Test
    void ordersTest() {
        CustomerGamma customerGamma = getCustomerGammaRandomSampleGenerator();
        OrderGamma orderGammaBack = getOrderGammaRandomSampleGenerator();

        customerGamma.addOrders(orderGammaBack);
        assertThat(customerGamma.getOrders()).containsOnly(orderGammaBack);
        assertThat(orderGammaBack.getCustomer()).isEqualTo(customerGamma);

        customerGamma.removeOrders(orderGammaBack);
        assertThat(customerGamma.getOrders()).doesNotContain(orderGammaBack);
        assertThat(orderGammaBack.getCustomer()).isNull();

        customerGamma.orders(new HashSet<>(Set.of(orderGammaBack)));
        assertThat(customerGamma.getOrders()).containsOnly(orderGammaBack);
        assertThat(orderGammaBack.getCustomer()).isEqualTo(customerGamma);

        customerGamma.setOrders(new HashSet<>());
        assertThat(customerGamma.getOrders()).doesNotContain(orderGammaBack);
        assertThat(orderGammaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        CustomerGamma customerGamma = getCustomerGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customerGamma.setTenant(masterTenantBack);
        assertThat(customerGamma.getTenant()).isEqualTo(masterTenantBack);

        customerGamma.tenant(null);
        assertThat(customerGamma.getTenant()).isNull();
    }
}
