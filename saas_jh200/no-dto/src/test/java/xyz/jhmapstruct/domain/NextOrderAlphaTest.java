package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentAlphaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderAlpha.class);
        NextOrderAlpha nextOrderAlpha1 = getNextOrderAlphaSample1();
        NextOrderAlpha nextOrderAlpha2 = new NextOrderAlpha();
        assertThat(nextOrderAlpha1).isNotEqualTo(nextOrderAlpha2);

        nextOrderAlpha2.setId(nextOrderAlpha1.getId());
        assertThat(nextOrderAlpha1).isEqualTo(nextOrderAlpha2);

        nextOrderAlpha2 = getNextOrderAlphaSample2();
        assertThat(nextOrderAlpha1).isNotEqualTo(nextOrderAlpha2);
    }

    @Test
    void productsTest() {
        NextOrderAlpha nextOrderAlpha = getNextOrderAlphaRandomSampleGenerator();
        NextProductAlpha nextProductAlphaBack = getNextProductAlphaRandomSampleGenerator();

        nextOrderAlpha.addProducts(nextProductAlphaBack);
        assertThat(nextOrderAlpha.getProducts()).containsOnly(nextProductAlphaBack);
        assertThat(nextProductAlphaBack.getOrder()).isEqualTo(nextOrderAlpha);

        nextOrderAlpha.removeProducts(nextProductAlphaBack);
        assertThat(nextOrderAlpha.getProducts()).doesNotContain(nextProductAlphaBack);
        assertThat(nextProductAlphaBack.getOrder()).isNull();

        nextOrderAlpha.products(new HashSet<>(Set.of(nextProductAlphaBack)));
        assertThat(nextOrderAlpha.getProducts()).containsOnly(nextProductAlphaBack);
        assertThat(nextProductAlphaBack.getOrder()).isEqualTo(nextOrderAlpha);

        nextOrderAlpha.setProducts(new HashSet<>());
        assertThat(nextOrderAlpha.getProducts()).doesNotContain(nextProductAlphaBack);
        assertThat(nextProductAlphaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderAlpha nextOrderAlpha = getNextOrderAlphaRandomSampleGenerator();
        NextPaymentAlpha nextPaymentAlphaBack = getNextPaymentAlphaRandomSampleGenerator();

        nextOrderAlpha.setPayment(nextPaymentAlphaBack);
        assertThat(nextOrderAlpha.getPayment()).isEqualTo(nextPaymentAlphaBack);

        nextOrderAlpha.payment(null);
        assertThat(nextOrderAlpha.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderAlpha nextOrderAlpha = getNextOrderAlphaRandomSampleGenerator();
        NextShipmentAlpha nextShipmentAlphaBack = getNextShipmentAlphaRandomSampleGenerator();

        nextOrderAlpha.setShipment(nextShipmentAlphaBack);
        assertThat(nextOrderAlpha.getShipment()).isEqualTo(nextShipmentAlphaBack);

        nextOrderAlpha.shipment(null);
        assertThat(nextOrderAlpha.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderAlpha nextOrderAlpha = getNextOrderAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderAlpha.setTenant(masterTenantBack);
        assertThat(nextOrderAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextOrderAlpha.tenant(null);
        assertThat(nextOrderAlpha.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderAlpha nextOrderAlpha = getNextOrderAlphaRandomSampleGenerator();
        NextCustomerAlpha nextCustomerAlphaBack = getNextCustomerAlphaRandomSampleGenerator();

        nextOrderAlpha.setCustomer(nextCustomerAlphaBack);
        assertThat(nextOrderAlpha.getCustomer()).isEqualTo(nextCustomerAlphaBack);

        nextOrderAlpha.customer(null);
        assertThat(nextOrderAlpha.getCustomer()).isNull();
    }
}
