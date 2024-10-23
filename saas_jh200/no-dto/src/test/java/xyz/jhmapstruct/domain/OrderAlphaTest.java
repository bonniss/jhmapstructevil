package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentAlphaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderAlpha.class);
        OrderAlpha orderAlpha1 = getOrderAlphaSample1();
        OrderAlpha orderAlpha2 = new OrderAlpha();
        assertThat(orderAlpha1).isNotEqualTo(orderAlpha2);

        orderAlpha2.setId(orderAlpha1.getId());
        assertThat(orderAlpha1).isEqualTo(orderAlpha2);

        orderAlpha2 = getOrderAlphaSample2();
        assertThat(orderAlpha1).isNotEqualTo(orderAlpha2);
    }

    @Test
    void productsTest() {
        OrderAlpha orderAlpha = getOrderAlphaRandomSampleGenerator();
        ProductAlpha productAlphaBack = getProductAlphaRandomSampleGenerator();

        orderAlpha.addProducts(productAlphaBack);
        assertThat(orderAlpha.getProducts()).containsOnly(productAlphaBack);
        assertThat(productAlphaBack.getOrder()).isEqualTo(orderAlpha);

        orderAlpha.removeProducts(productAlphaBack);
        assertThat(orderAlpha.getProducts()).doesNotContain(productAlphaBack);
        assertThat(productAlphaBack.getOrder()).isNull();

        orderAlpha.products(new HashSet<>(Set.of(productAlphaBack)));
        assertThat(orderAlpha.getProducts()).containsOnly(productAlphaBack);
        assertThat(productAlphaBack.getOrder()).isEqualTo(orderAlpha);

        orderAlpha.setProducts(new HashSet<>());
        assertThat(orderAlpha.getProducts()).doesNotContain(productAlphaBack);
        assertThat(productAlphaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderAlpha orderAlpha = getOrderAlphaRandomSampleGenerator();
        PaymentAlpha paymentAlphaBack = getPaymentAlphaRandomSampleGenerator();

        orderAlpha.setPayment(paymentAlphaBack);
        assertThat(orderAlpha.getPayment()).isEqualTo(paymentAlphaBack);

        orderAlpha.payment(null);
        assertThat(orderAlpha.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderAlpha orderAlpha = getOrderAlphaRandomSampleGenerator();
        ShipmentAlpha shipmentAlphaBack = getShipmentAlphaRandomSampleGenerator();

        orderAlpha.setShipment(shipmentAlphaBack);
        assertThat(orderAlpha.getShipment()).isEqualTo(shipmentAlphaBack);

        orderAlpha.shipment(null);
        assertThat(orderAlpha.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        OrderAlpha orderAlpha = getOrderAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        orderAlpha.setTenant(masterTenantBack);
        assertThat(orderAlpha.getTenant()).isEqualTo(masterTenantBack);

        orderAlpha.tenant(null);
        assertThat(orderAlpha.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        OrderAlpha orderAlpha = getOrderAlphaRandomSampleGenerator();
        CustomerAlpha customerAlphaBack = getCustomerAlphaRandomSampleGenerator();

        orderAlpha.setCustomer(customerAlphaBack);
        assertThat(orderAlpha.getCustomer()).isEqualTo(customerAlphaBack);

        orderAlpha.customer(null);
        assertThat(orderAlpha.getCustomer()).isNull();
    }
}
