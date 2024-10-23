package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerGammaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderGammaTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentGammaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductGammaTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentGammaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderGamma.class);
        OrderGamma orderGamma1 = getOrderGammaSample1();
        OrderGamma orderGamma2 = new OrderGamma();
        assertThat(orderGamma1).isNotEqualTo(orderGamma2);

        orderGamma2.setId(orderGamma1.getId());
        assertThat(orderGamma1).isEqualTo(orderGamma2);

        orderGamma2 = getOrderGammaSample2();
        assertThat(orderGamma1).isNotEqualTo(orderGamma2);
    }

    @Test
    void productsTest() {
        OrderGamma orderGamma = getOrderGammaRandomSampleGenerator();
        ProductGamma productGammaBack = getProductGammaRandomSampleGenerator();

        orderGamma.addProducts(productGammaBack);
        assertThat(orderGamma.getProducts()).containsOnly(productGammaBack);
        assertThat(productGammaBack.getOrder()).isEqualTo(orderGamma);

        orderGamma.removeProducts(productGammaBack);
        assertThat(orderGamma.getProducts()).doesNotContain(productGammaBack);
        assertThat(productGammaBack.getOrder()).isNull();

        orderGamma.products(new HashSet<>(Set.of(productGammaBack)));
        assertThat(orderGamma.getProducts()).containsOnly(productGammaBack);
        assertThat(productGammaBack.getOrder()).isEqualTo(orderGamma);

        orderGamma.setProducts(new HashSet<>());
        assertThat(orderGamma.getProducts()).doesNotContain(productGammaBack);
        assertThat(productGammaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderGamma orderGamma = getOrderGammaRandomSampleGenerator();
        PaymentGamma paymentGammaBack = getPaymentGammaRandomSampleGenerator();

        orderGamma.setPayment(paymentGammaBack);
        assertThat(orderGamma.getPayment()).isEqualTo(paymentGammaBack);

        orderGamma.payment(null);
        assertThat(orderGamma.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderGamma orderGamma = getOrderGammaRandomSampleGenerator();
        ShipmentGamma shipmentGammaBack = getShipmentGammaRandomSampleGenerator();

        orderGamma.setShipment(shipmentGammaBack);
        assertThat(orderGamma.getShipment()).isEqualTo(shipmentGammaBack);

        orderGamma.shipment(null);
        assertThat(orderGamma.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        OrderGamma orderGamma = getOrderGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        orderGamma.setTenant(masterTenantBack);
        assertThat(orderGamma.getTenant()).isEqualTo(masterTenantBack);

        orderGamma.tenant(null);
        assertThat(orderGamma.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        OrderGamma orderGamma = getOrderGammaRandomSampleGenerator();
        CustomerGamma customerGammaBack = getCustomerGammaRandomSampleGenerator();

        orderGamma.setCustomer(customerGammaBack);
        assertThat(orderGamma.getCustomer()).isEqualTo(customerGammaBack);

        orderGamma.customer(null);
        assertThat(orderGamma.getCustomer()).isNull();
    }
}
