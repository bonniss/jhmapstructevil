package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerTestSamples.*;
import static xyz.jhmapstruct.domain.OrderTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentTestSamples.*;
import static xyz.jhmapstruct.domain.ProductTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = getOrderSample1();
        Order order2 = new Order();
        assertThat(order1).isNotEqualTo(order2);

        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);

        order2 = getOrderSample2();
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    void productsTest() {
        Order order = getOrderRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        order.addProducts(productBack);
        assertThat(order.getProducts()).containsOnly(productBack);
        assertThat(productBack.getOrder()).isEqualTo(order);

        order.removeProducts(productBack);
        assertThat(order.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getOrder()).isNull();

        order.products(new HashSet<>(Set.of(productBack)));
        assertThat(order.getProducts()).containsOnly(productBack);
        assertThat(productBack.getOrder()).isEqualTo(order);

        order.setProducts(new HashSet<>());
        assertThat(order.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        Order order = getOrderRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        order.setPayment(paymentBack);
        assertThat(order.getPayment()).isEqualTo(paymentBack);

        order.payment(null);
        assertThat(order.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        Order order = getOrderRandomSampleGenerator();
        Shipment shipmentBack = getShipmentRandomSampleGenerator();

        order.setShipment(shipmentBack);
        assertThat(order.getShipment()).isEqualTo(shipmentBack);

        order.shipment(null);
        assertThat(order.getShipment()).isNull();
    }

    @Test
    void customerTest() {
        Order order = getOrderRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        order.setCustomer(customerBack);
        assertThat(order.getCustomer()).isEqualTo(customerBack);

        order.customer(null);
        assertThat(order.getCustomer()).isNull();
    }
}
