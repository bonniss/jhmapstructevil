package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerThetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderThetaTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentThetaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductThetaTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentThetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderTheta.class);
        OrderTheta orderTheta1 = getOrderThetaSample1();
        OrderTheta orderTheta2 = new OrderTheta();
        assertThat(orderTheta1).isNotEqualTo(orderTheta2);

        orderTheta2.setId(orderTheta1.getId());
        assertThat(orderTheta1).isEqualTo(orderTheta2);

        orderTheta2 = getOrderThetaSample2();
        assertThat(orderTheta1).isNotEqualTo(orderTheta2);
    }

    @Test
    void productsTest() {
        OrderTheta orderTheta = getOrderThetaRandomSampleGenerator();
        ProductTheta productThetaBack = getProductThetaRandomSampleGenerator();

        orderTheta.addProducts(productThetaBack);
        assertThat(orderTheta.getProducts()).containsOnly(productThetaBack);
        assertThat(productThetaBack.getOrder()).isEqualTo(orderTheta);

        orderTheta.removeProducts(productThetaBack);
        assertThat(orderTheta.getProducts()).doesNotContain(productThetaBack);
        assertThat(productThetaBack.getOrder()).isNull();

        orderTheta.products(new HashSet<>(Set.of(productThetaBack)));
        assertThat(orderTheta.getProducts()).containsOnly(productThetaBack);
        assertThat(productThetaBack.getOrder()).isEqualTo(orderTheta);

        orderTheta.setProducts(new HashSet<>());
        assertThat(orderTheta.getProducts()).doesNotContain(productThetaBack);
        assertThat(productThetaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderTheta orderTheta = getOrderThetaRandomSampleGenerator();
        PaymentTheta paymentThetaBack = getPaymentThetaRandomSampleGenerator();

        orderTheta.setPayment(paymentThetaBack);
        assertThat(orderTheta.getPayment()).isEqualTo(paymentThetaBack);

        orderTheta.payment(null);
        assertThat(orderTheta.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderTheta orderTheta = getOrderThetaRandomSampleGenerator();
        ShipmentTheta shipmentThetaBack = getShipmentThetaRandomSampleGenerator();

        orderTheta.setShipment(shipmentThetaBack);
        assertThat(orderTheta.getShipment()).isEqualTo(shipmentThetaBack);

        orderTheta.shipment(null);
        assertThat(orderTheta.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        OrderTheta orderTheta = getOrderThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        orderTheta.setTenant(masterTenantBack);
        assertThat(orderTheta.getTenant()).isEqualTo(masterTenantBack);

        orderTheta.tenant(null);
        assertThat(orderTheta.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        OrderTheta orderTheta = getOrderThetaRandomSampleGenerator();
        CustomerTheta customerThetaBack = getCustomerThetaRandomSampleGenerator();

        orderTheta.setCustomer(customerThetaBack);
        assertThat(orderTheta.getCustomer()).isEqualTo(customerThetaBack);

        orderTheta.customer(null);
        assertThat(orderTheta.getCustomer()).isNull();
    }
}
