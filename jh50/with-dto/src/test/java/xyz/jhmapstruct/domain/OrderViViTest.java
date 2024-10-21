package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerViViTestSamples.*;
import static xyz.jhmapstruct.domain.OrderViViTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentViViTestSamples.*;
import static xyz.jhmapstruct.domain.ProductViViTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentViViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderViVi.class);
        OrderViVi orderViVi1 = getOrderViViSample1();
        OrderViVi orderViVi2 = new OrderViVi();
        assertThat(orderViVi1).isNotEqualTo(orderViVi2);

        orderViVi2.setId(orderViVi1.getId());
        assertThat(orderViVi1).isEqualTo(orderViVi2);

        orderViVi2 = getOrderViViSample2();
        assertThat(orderViVi1).isNotEqualTo(orderViVi2);
    }

    @Test
    void productsTest() {
        OrderViVi orderViVi = getOrderViViRandomSampleGenerator();
        ProductViVi productViViBack = getProductViViRandomSampleGenerator();

        orderViVi.addProducts(productViViBack);
        assertThat(orderViVi.getProducts()).containsOnly(productViViBack);
        assertThat(productViViBack.getOrder()).isEqualTo(orderViVi);

        orderViVi.removeProducts(productViViBack);
        assertThat(orderViVi.getProducts()).doesNotContain(productViViBack);
        assertThat(productViViBack.getOrder()).isNull();

        orderViVi.products(new HashSet<>(Set.of(productViViBack)));
        assertThat(orderViVi.getProducts()).containsOnly(productViViBack);
        assertThat(productViViBack.getOrder()).isEqualTo(orderViVi);

        orderViVi.setProducts(new HashSet<>());
        assertThat(orderViVi.getProducts()).doesNotContain(productViViBack);
        assertThat(productViViBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderViVi orderViVi = getOrderViViRandomSampleGenerator();
        PaymentViVi paymentViViBack = getPaymentViViRandomSampleGenerator();

        orderViVi.setPayment(paymentViViBack);
        assertThat(orderViVi.getPayment()).isEqualTo(paymentViViBack);

        orderViVi.payment(null);
        assertThat(orderViVi.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderViVi orderViVi = getOrderViViRandomSampleGenerator();
        ShipmentViVi shipmentViViBack = getShipmentViViRandomSampleGenerator();

        orderViVi.setShipment(shipmentViViBack);
        assertThat(orderViVi.getShipment()).isEqualTo(shipmentViViBack);

        orderViVi.shipment(null);
        assertThat(orderViVi.getShipment()).isNull();
    }

    @Test
    void customerTest() {
        OrderViVi orderViVi = getOrderViViRandomSampleGenerator();
        CustomerViVi customerViViBack = getCustomerViViRandomSampleGenerator();

        orderViVi.setCustomer(customerViViBack);
        assertThat(orderViVi.getCustomer()).isEqualTo(customerViViBack);

        orderViVi.customer(null);
        assertThat(orderViVi.getCustomer()).isNull();
    }
}
