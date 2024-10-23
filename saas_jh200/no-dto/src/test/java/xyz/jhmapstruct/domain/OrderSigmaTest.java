package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentSigmaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderSigma.class);
        OrderSigma orderSigma1 = getOrderSigmaSample1();
        OrderSigma orderSigma2 = new OrderSigma();
        assertThat(orderSigma1).isNotEqualTo(orderSigma2);

        orderSigma2.setId(orderSigma1.getId());
        assertThat(orderSigma1).isEqualTo(orderSigma2);

        orderSigma2 = getOrderSigmaSample2();
        assertThat(orderSigma1).isNotEqualTo(orderSigma2);
    }

    @Test
    void productsTest() {
        OrderSigma orderSigma = getOrderSigmaRandomSampleGenerator();
        ProductSigma productSigmaBack = getProductSigmaRandomSampleGenerator();

        orderSigma.addProducts(productSigmaBack);
        assertThat(orderSigma.getProducts()).containsOnly(productSigmaBack);
        assertThat(productSigmaBack.getOrder()).isEqualTo(orderSigma);

        orderSigma.removeProducts(productSigmaBack);
        assertThat(orderSigma.getProducts()).doesNotContain(productSigmaBack);
        assertThat(productSigmaBack.getOrder()).isNull();

        orderSigma.products(new HashSet<>(Set.of(productSigmaBack)));
        assertThat(orderSigma.getProducts()).containsOnly(productSigmaBack);
        assertThat(productSigmaBack.getOrder()).isEqualTo(orderSigma);

        orderSigma.setProducts(new HashSet<>());
        assertThat(orderSigma.getProducts()).doesNotContain(productSigmaBack);
        assertThat(productSigmaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderSigma orderSigma = getOrderSigmaRandomSampleGenerator();
        PaymentSigma paymentSigmaBack = getPaymentSigmaRandomSampleGenerator();

        orderSigma.setPayment(paymentSigmaBack);
        assertThat(orderSigma.getPayment()).isEqualTo(paymentSigmaBack);

        orderSigma.payment(null);
        assertThat(orderSigma.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderSigma orderSigma = getOrderSigmaRandomSampleGenerator();
        ShipmentSigma shipmentSigmaBack = getShipmentSigmaRandomSampleGenerator();

        orderSigma.setShipment(shipmentSigmaBack);
        assertThat(orderSigma.getShipment()).isEqualTo(shipmentSigmaBack);

        orderSigma.shipment(null);
        assertThat(orderSigma.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        OrderSigma orderSigma = getOrderSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        orderSigma.setTenant(masterTenantBack);
        assertThat(orderSigma.getTenant()).isEqualTo(masterTenantBack);

        orderSigma.tenant(null);
        assertThat(orderSigma.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        OrderSigma orderSigma = getOrderSigmaRandomSampleGenerator();
        CustomerSigma customerSigmaBack = getCustomerSigmaRandomSampleGenerator();

        orderSigma.setCustomer(customerSigmaBack);
        assertThat(orderSigma.getCustomer()).isEqualTo(customerSigmaBack);

        orderSigma.customer(null);
        assertThat(orderSigma.getCustomer()).isNull();
    }
}
