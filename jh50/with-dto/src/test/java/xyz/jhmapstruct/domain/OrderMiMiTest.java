package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.OrderMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentMiMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderMiMi.class);
        OrderMiMi orderMiMi1 = getOrderMiMiSample1();
        OrderMiMi orderMiMi2 = new OrderMiMi();
        assertThat(orderMiMi1).isNotEqualTo(orderMiMi2);

        orderMiMi2.setId(orderMiMi1.getId());
        assertThat(orderMiMi1).isEqualTo(orderMiMi2);

        orderMiMi2 = getOrderMiMiSample2();
        assertThat(orderMiMi1).isNotEqualTo(orderMiMi2);
    }

    @Test
    void productsTest() {
        OrderMiMi orderMiMi = getOrderMiMiRandomSampleGenerator();
        ProductMiMi productMiMiBack = getProductMiMiRandomSampleGenerator();

        orderMiMi.addProducts(productMiMiBack);
        assertThat(orderMiMi.getProducts()).containsOnly(productMiMiBack);
        assertThat(productMiMiBack.getOrder()).isEqualTo(orderMiMi);

        orderMiMi.removeProducts(productMiMiBack);
        assertThat(orderMiMi.getProducts()).doesNotContain(productMiMiBack);
        assertThat(productMiMiBack.getOrder()).isNull();

        orderMiMi.products(new HashSet<>(Set.of(productMiMiBack)));
        assertThat(orderMiMi.getProducts()).containsOnly(productMiMiBack);
        assertThat(productMiMiBack.getOrder()).isEqualTo(orderMiMi);

        orderMiMi.setProducts(new HashSet<>());
        assertThat(orderMiMi.getProducts()).doesNotContain(productMiMiBack);
        assertThat(productMiMiBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderMiMi orderMiMi = getOrderMiMiRandomSampleGenerator();
        PaymentMiMi paymentMiMiBack = getPaymentMiMiRandomSampleGenerator();

        orderMiMi.setPayment(paymentMiMiBack);
        assertThat(orderMiMi.getPayment()).isEqualTo(paymentMiMiBack);

        orderMiMi.payment(null);
        assertThat(orderMiMi.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderMiMi orderMiMi = getOrderMiMiRandomSampleGenerator();
        ShipmentMiMi shipmentMiMiBack = getShipmentMiMiRandomSampleGenerator();

        orderMiMi.setShipment(shipmentMiMiBack);
        assertThat(orderMiMi.getShipment()).isEqualTo(shipmentMiMiBack);

        orderMiMi.shipment(null);
        assertThat(orderMiMi.getShipment()).isNull();
    }

    @Test
    void customerTest() {
        OrderMiMi orderMiMi = getOrderMiMiRandomSampleGenerator();
        CustomerMiMi customerMiMiBack = getCustomerMiMiRandomSampleGenerator();

        orderMiMi.setCustomer(customerMiMiBack);
        assertThat(orderMiMi.getCustomer()).isEqualTo(customerMiMiBack);

        orderMiMi.customer(null);
        assertThat(orderMiMi.getCustomer()).isNull();
    }
}
