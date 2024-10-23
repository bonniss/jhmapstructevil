package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentBetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderBeta.class);
        NextOrderBeta nextOrderBeta1 = getNextOrderBetaSample1();
        NextOrderBeta nextOrderBeta2 = new NextOrderBeta();
        assertThat(nextOrderBeta1).isNotEqualTo(nextOrderBeta2);

        nextOrderBeta2.setId(nextOrderBeta1.getId());
        assertThat(nextOrderBeta1).isEqualTo(nextOrderBeta2);

        nextOrderBeta2 = getNextOrderBetaSample2();
        assertThat(nextOrderBeta1).isNotEqualTo(nextOrderBeta2);
    }

    @Test
    void productsTest() {
        NextOrderBeta nextOrderBeta = getNextOrderBetaRandomSampleGenerator();
        NextProductBeta nextProductBetaBack = getNextProductBetaRandomSampleGenerator();

        nextOrderBeta.addProducts(nextProductBetaBack);
        assertThat(nextOrderBeta.getProducts()).containsOnly(nextProductBetaBack);
        assertThat(nextProductBetaBack.getOrder()).isEqualTo(nextOrderBeta);

        nextOrderBeta.removeProducts(nextProductBetaBack);
        assertThat(nextOrderBeta.getProducts()).doesNotContain(nextProductBetaBack);
        assertThat(nextProductBetaBack.getOrder()).isNull();

        nextOrderBeta.products(new HashSet<>(Set.of(nextProductBetaBack)));
        assertThat(nextOrderBeta.getProducts()).containsOnly(nextProductBetaBack);
        assertThat(nextProductBetaBack.getOrder()).isEqualTo(nextOrderBeta);

        nextOrderBeta.setProducts(new HashSet<>());
        assertThat(nextOrderBeta.getProducts()).doesNotContain(nextProductBetaBack);
        assertThat(nextProductBetaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderBeta nextOrderBeta = getNextOrderBetaRandomSampleGenerator();
        NextPaymentBeta nextPaymentBetaBack = getNextPaymentBetaRandomSampleGenerator();

        nextOrderBeta.setPayment(nextPaymentBetaBack);
        assertThat(nextOrderBeta.getPayment()).isEqualTo(nextPaymentBetaBack);

        nextOrderBeta.payment(null);
        assertThat(nextOrderBeta.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderBeta nextOrderBeta = getNextOrderBetaRandomSampleGenerator();
        NextShipmentBeta nextShipmentBetaBack = getNextShipmentBetaRandomSampleGenerator();

        nextOrderBeta.setShipment(nextShipmentBetaBack);
        assertThat(nextOrderBeta.getShipment()).isEqualTo(nextShipmentBetaBack);

        nextOrderBeta.shipment(null);
        assertThat(nextOrderBeta.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderBeta nextOrderBeta = getNextOrderBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderBeta.setTenant(masterTenantBack);
        assertThat(nextOrderBeta.getTenant()).isEqualTo(masterTenantBack);

        nextOrderBeta.tenant(null);
        assertThat(nextOrderBeta.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderBeta nextOrderBeta = getNextOrderBetaRandomSampleGenerator();
        NextCustomerBeta nextCustomerBetaBack = getNextCustomerBetaRandomSampleGenerator();

        nextOrderBeta.setCustomer(nextCustomerBetaBack);
        assertThat(nextOrderBeta.getCustomer()).isEqualTo(nextCustomerBetaBack);

        nextOrderBeta.customer(null);
        assertThat(nextOrderBeta.getCustomer()).isNull();
    }
}
