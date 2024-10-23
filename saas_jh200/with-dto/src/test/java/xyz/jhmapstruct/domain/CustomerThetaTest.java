package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerThetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderThetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerTheta.class);
        CustomerTheta customerTheta1 = getCustomerThetaSample1();
        CustomerTheta customerTheta2 = new CustomerTheta();
        assertThat(customerTheta1).isNotEqualTo(customerTheta2);

        customerTheta2.setId(customerTheta1.getId());
        assertThat(customerTheta1).isEqualTo(customerTheta2);

        customerTheta2 = getCustomerThetaSample2();
        assertThat(customerTheta1).isNotEqualTo(customerTheta2);
    }

    @Test
    void ordersTest() {
        CustomerTheta customerTheta = getCustomerThetaRandomSampleGenerator();
        OrderTheta orderThetaBack = getOrderThetaRandomSampleGenerator();

        customerTheta.addOrders(orderThetaBack);
        assertThat(customerTheta.getOrders()).containsOnly(orderThetaBack);
        assertThat(orderThetaBack.getCustomer()).isEqualTo(customerTheta);

        customerTheta.removeOrders(orderThetaBack);
        assertThat(customerTheta.getOrders()).doesNotContain(orderThetaBack);
        assertThat(orderThetaBack.getCustomer()).isNull();

        customerTheta.orders(new HashSet<>(Set.of(orderThetaBack)));
        assertThat(customerTheta.getOrders()).containsOnly(orderThetaBack);
        assertThat(orderThetaBack.getCustomer()).isEqualTo(customerTheta);

        customerTheta.setOrders(new HashSet<>());
        assertThat(customerTheta.getOrders()).doesNotContain(orderThetaBack);
        assertThat(orderThetaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        CustomerTheta customerTheta = getCustomerThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        customerTheta.setTenant(masterTenantBack);
        assertThat(customerTheta.getTenant()).isEqualTo(masterTenantBack);

        customerTheta.tenant(null);
        assertThat(customerTheta.getTenant()).isNull();
    }
}
