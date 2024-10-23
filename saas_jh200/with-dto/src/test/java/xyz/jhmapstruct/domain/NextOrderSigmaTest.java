package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentSigmaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderSigma.class);
        NextOrderSigma nextOrderSigma1 = getNextOrderSigmaSample1();
        NextOrderSigma nextOrderSigma2 = new NextOrderSigma();
        assertThat(nextOrderSigma1).isNotEqualTo(nextOrderSigma2);

        nextOrderSigma2.setId(nextOrderSigma1.getId());
        assertThat(nextOrderSigma1).isEqualTo(nextOrderSigma2);

        nextOrderSigma2 = getNextOrderSigmaSample2();
        assertThat(nextOrderSigma1).isNotEqualTo(nextOrderSigma2);
    }

    @Test
    void productsTest() {
        NextOrderSigma nextOrderSigma = getNextOrderSigmaRandomSampleGenerator();
        NextProductSigma nextProductSigmaBack = getNextProductSigmaRandomSampleGenerator();

        nextOrderSigma.addProducts(nextProductSigmaBack);
        assertThat(nextOrderSigma.getProducts()).containsOnly(nextProductSigmaBack);
        assertThat(nextProductSigmaBack.getOrder()).isEqualTo(nextOrderSigma);

        nextOrderSigma.removeProducts(nextProductSigmaBack);
        assertThat(nextOrderSigma.getProducts()).doesNotContain(nextProductSigmaBack);
        assertThat(nextProductSigmaBack.getOrder()).isNull();

        nextOrderSigma.products(new HashSet<>(Set.of(nextProductSigmaBack)));
        assertThat(nextOrderSigma.getProducts()).containsOnly(nextProductSigmaBack);
        assertThat(nextProductSigmaBack.getOrder()).isEqualTo(nextOrderSigma);

        nextOrderSigma.setProducts(new HashSet<>());
        assertThat(nextOrderSigma.getProducts()).doesNotContain(nextProductSigmaBack);
        assertThat(nextProductSigmaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderSigma nextOrderSigma = getNextOrderSigmaRandomSampleGenerator();
        NextPaymentSigma nextPaymentSigmaBack = getNextPaymentSigmaRandomSampleGenerator();

        nextOrderSigma.setPayment(nextPaymentSigmaBack);
        assertThat(nextOrderSigma.getPayment()).isEqualTo(nextPaymentSigmaBack);

        nextOrderSigma.payment(null);
        assertThat(nextOrderSigma.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderSigma nextOrderSigma = getNextOrderSigmaRandomSampleGenerator();
        NextShipmentSigma nextShipmentSigmaBack = getNextShipmentSigmaRandomSampleGenerator();

        nextOrderSigma.setShipment(nextShipmentSigmaBack);
        assertThat(nextOrderSigma.getShipment()).isEqualTo(nextShipmentSigmaBack);

        nextOrderSigma.shipment(null);
        assertThat(nextOrderSigma.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderSigma nextOrderSigma = getNextOrderSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderSigma.setTenant(masterTenantBack);
        assertThat(nextOrderSigma.getTenant()).isEqualTo(masterTenantBack);

        nextOrderSigma.tenant(null);
        assertThat(nextOrderSigma.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderSigma nextOrderSigma = getNextOrderSigmaRandomSampleGenerator();
        NextCustomerSigma nextCustomerSigmaBack = getNextCustomerSigmaRandomSampleGenerator();

        nextOrderSigma.setCustomer(nextCustomerSigmaBack);
        assertThat(nextOrderSigma.getCustomer()).isEqualTo(nextCustomerSigmaBack);

        nextOrderSigma.customer(null);
        assertThat(nextOrderSigma.getCustomer()).isNull();
    }
}
