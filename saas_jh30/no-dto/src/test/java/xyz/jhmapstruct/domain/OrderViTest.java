package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderViTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentViTestSamples.*;
import static xyz.jhmapstruct.domain.ProductViTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderVi.class);
        OrderVi orderVi1 = getOrderViSample1();
        OrderVi orderVi2 = new OrderVi();
        assertThat(orderVi1).isNotEqualTo(orderVi2);

        orderVi2.setId(orderVi1.getId());
        assertThat(orderVi1).isEqualTo(orderVi2);

        orderVi2 = getOrderViSample2();
        assertThat(orderVi1).isNotEqualTo(orderVi2);
    }

    @Test
    void productsTest() {
        OrderVi orderVi = getOrderViRandomSampleGenerator();
        ProductVi productViBack = getProductViRandomSampleGenerator();

        orderVi.addProducts(productViBack);
        assertThat(orderVi.getProducts()).containsOnly(productViBack);
        assertThat(productViBack.getOrder()).isEqualTo(orderVi);

        orderVi.removeProducts(productViBack);
        assertThat(orderVi.getProducts()).doesNotContain(productViBack);
        assertThat(productViBack.getOrder()).isNull();

        orderVi.products(new HashSet<>(Set.of(productViBack)));
        assertThat(orderVi.getProducts()).containsOnly(productViBack);
        assertThat(productViBack.getOrder()).isEqualTo(orderVi);

        orderVi.setProducts(new HashSet<>());
        assertThat(orderVi.getProducts()).doesNotContain(productViBack);
        assertThat(productViBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderVi orderVi = getOrderViRandomSampleGenerator();
        PaymentVi paymentViBack = getPaymentViRandomSampleGenerator();

        orderVi.setPayment(paymentViBack);
        assertThat(orderVi.getPayment()).isEqualTo(paymentViBack);

        orderVi.payment(null);
        assertThat(orderVi.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderVi orderVi = getOrderViRandomSampleGenerator();
        ShipmentVi shipmentViBack = getShipmentViRandomSampleGenerator();

        orderVi.setShipment(shipmentViBack);
        assertThat(orderVi.getShipment()).isEqualTo(shipmentViBack);

        orderVi.shipment(null);
        assertThat(orderVi.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        OrderVi orderVi = getOrderViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        orderVi.setTenant(masterTenantBack);
        assertThat(orderVi.getTenant()).isEqualTo(masterTenantBack);

        orderVi.tenant(null);
        assertThat(orderVi.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        OrderVi orderVi = getOrderViRandomSampleGenerator();
        CustomerVi customerViBack = getCustomerViRandomSampleGenerator();

        orderVi.setCustomer(customerViBack);
        assertThat(orderVi.getCustomer()).isEqualTo(customerViBack);

        orderVi.customer(null);
        assertThat(orderVi.getCustomer()).isNull();
    }
}
