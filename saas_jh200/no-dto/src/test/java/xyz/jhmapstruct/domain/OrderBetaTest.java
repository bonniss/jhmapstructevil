package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerBetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderBetaTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentBetaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductBetaTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentBetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderBeta.class);
        OrderBeta orderBeta1 = getOrderBetaSample1();
        OrderBeta orderBeta2 = new OrderBeta();
        assertThat(orderBeta1).isNotEqualTo(orderBeta2);

        orderBeta2.setId(orderBeta1.getId());
        assertThat(orderBeta1).isEqualTo(orderBeta2);

        orderBeta2 = getOrderBetaSample2();
        assertThat(orderBeta1).isNotEqualTo(orderBeta2);
    }

    @Test
    void productsTest() {
        OrderBeta orderBeta = getOrderBetaRandomSampleGenerator();
        ProductBeta productBetaBack = getProductBetaRandomSampleGenerator();

        orderBeta.addProducts(productBetaBack);
        assertThat(orderBeta.getProducts()).containsOnly(productBetaBack);
        assertThat(productBetaBack.getOrder()).isEqualTo(orderBeta);

        orderBeta.removeProducts(productBetaBack);
        assertThat(orderBeta.getProducts()).doesNotContain(productBetaBack);
        assertThat(productBetaBack.getOrder()).isNull();

        orderBeta.products(new HashSet<>(Set.of(productBetaBack)));
        assertThat(orderBeta.getProducts()).containsOnly(productBetaBack);
        assertThat(productBetaBack.getOrder()).isEqualTo(orderBeta);

        orderBeta.setProducts(new HashSet<>());
        assertThat(orderBeta.getProducts()).doesNotContain(productBetaBack);
        assertThat(productBetaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        OrderBeta orderBeta = getOrderBetaRandomSampleGenerator();
        PaymentBeta paymentBetaBack = getPaymentBetaRandomSampleGenerator();

        orderBeta.setPayment(paymentBetaBack);
        assertThat(orderBeta.getPayment()).isEqualTo(paymentBetaBack);

        orderBeta.payment(null);
        assertThat(orderBeta.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        OrderBeta orderBeta = getOrderBetaRandomSampleGenerator();
        ShipmentBeta shipmentBetaBack = getShipmentBetaRandomSampleGenerator();

        orderBeta.setShipment(shipmentBetaBack);
        assertThat(orderBeta.getShipment()).isEqualTo(shipmentBetaBack);

        orderBeta.shipment(null);
        assertThat(orderBeta.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        OrderBeta orderBeta = getOrderBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        orderBeta.setTenant(masterTenantBack);
        assertThat(orderBeta.getTenant()).isEqualTo(masterTenantBack);

        orderBeta.tenant(null);
        assertThat(orderBeta.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        OrderBeta orderBeta = getOrderBetaRandomSampleGenerator();
        CustomerBeta customerBetaBack = getCustomerBetaRandomSampleGenerator();

        orderBeta.setCustomer(customerBetaBack);
        assertThat(orderBeta.getCustomer()).isEqualTo(customerBetaBack);

        orderBeta.customer(null);
        assertThat(orderBeta.getCustomer()).isNull();
    }
}
