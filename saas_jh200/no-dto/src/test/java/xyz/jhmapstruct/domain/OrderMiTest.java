package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentMiTestSamples.*;
import static xyz.jhmapstruct.domain.OrderMiTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderMi.class);
        OrderMi orderMi1 = getOrderMiSample1();
        OrderMi orderMi2 = new OrderMi();
        assertThat(orderMi1).isNotEqualTo(orderMi2);

        orderMi2.setId(orderMi1.getId());
        assertThat(orderMi1).isEqualTo(orderMi2);

        orderMi2 = getOrderMiSample2();
        assertThat(orderMi1).isNotEqualTo(orderMi2);
    }

    @Test
    void productsTest() {
        OrderMi orderMi = getOrderMiRandomSampleGenerator();
        ProductMi productMiBack = getProductMiRandomSampleGenerator();

        orderMi.addProducts(productMiBack);
        assertThat(orderMi.getProducts()).containsOnly(productMiBack);
        assertThat(productMiBack.getOrder()).isEqualTo(orderMi);

        orderMi.removeProducts(productMiBack);
        assertThat(orderMi.getProducts()).doesNotContain(productMiBack);
        assertThat(productMiBack.getOrder()).isNull();

        orderMi.products(new HashSet<>(Set.of(productMiBack)));
        assertThat(orderMi.getProducts()).containsOnly(productMiBack);
        assertThat(productMiBack.getOrder()).isEqualTo(orderMi);

        orderMi.setProducts(new HashSet<>());
        assertThat(orderMi.getProducts()).doesNotContain(productMiBack);
        assertThat(productMiBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderMi orderMi = getOrderMiRandomSampleGenerator();
        NextPaymentMi nextPaymentMiBack = getNextPaymentMiRandomSampleGenerator();

        orderMi.setPayment(nextPaymentMiBack);
        assertThat(orderMi.getPayment()).isEqualTo(nextPaymentMiBack);

        orderMi.payment(null);
        assertThat(orderMi.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderMi orderMi = getOrderMiRandomSampleGenerator();
        NextShipmentMi nextShipmentMiBack = getNextShipmentMiRandomSampleGenerator();

        orderMi.setShipment(nextShipmentMiBack);
        assertThat(orderMi.getShipment()).isEqualTo(nextShipmentMiBack);

        orderMi.shipment(null);
        assertThat(orderMi.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        OrderMi orderMi = getOrderMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        orderMi.setTenant(masterTenantBack);
        assertThat(orderMi.getTenant()).isEqualTo(masterTenantBack);

        orderMi.tenant(null);
        assertThat(orderMi.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        OrderMi orderMi = getOrderMiRandomSampleGenerator();
        NextCustomerMi nextCustomerMiBack = getNextCustomerMiRandomSampleGenerator();

        orderMi.setCustomer(nextCustomerMiBack);
        assertThat(orderMi.getCustomer()).isEqualTo(nextCustomerMiBack);

        orderMi.customer(null);
        assertThat(orderMi.getCustomer()).isNull();
    }
}
