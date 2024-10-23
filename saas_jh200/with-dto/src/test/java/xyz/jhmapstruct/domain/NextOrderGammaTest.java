package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentGammaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderGamma.class);
        NextOrderGamma nextOrderGamma1 = getNextOrderGammaSample1();
        NextOrderGamma nextOrderGamma2 = new NextOrderGamma();
        assertThat(nextOrderGamma1).isNotEqualTo(nextOrderGamma2);

        nextOrderGamma2.setId(nextOrderGamma1.getId());
        assertThat(nextOrderGamma1).isEqualTo(nextOrderGamma2);

        nextOrderGamma2 = getNextOrderGammaSample2();
        assertThat(nextOrderGamma1).isNotEqualTo(nextOrderGamma2);
    }

    @Test
    void productsTest() {
        NextOrderGamma nextOrderGamma = getNextOrderGammaRandomSampleGenerator();
        NextProductGamma nextProductGammaBack = getNextProductGammaRandomSampleGenerator();

        nextOrderGamma.addProducts(nextProductGammaBack);
        assertThat(nextOrderGamma.getProducts()).containsOnly(nextProductGammaBack);
        assertThat(nextProductGammaBack.getOrder()).isEqualTo(nextOrderGamma);

        nextOrderGamma.removeProducts(nextProductGammaBack);
        assertThat(nextOrderGamma.getProducts()).doesNotContain(nextProductGammaBack);
        assertThat(nextProductGammaBack.getOrder()).isNull();

        nextOrderGamma.products(new HashSet<>(Set.of(nextProductGammaBack)));
        assertThat(nextOrderGamma.getProducts()).containsOnly(nextProductGammaBack);
        assertThat(nextProductGammaBack.getOrder()).isEqualTo(nextOrderGamma);

        nextOrderGamma.setProducts(new HashSet<>());
        assertThat(nextOrderGamma.getProducts()).doesNotContain(nextProductGammaBack);
        assertThat(nextProductGammaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderGamma nextOrderGamma = getNextOrderGammaRandomSampleGenerator();
        NextPaymentGamma nextPaymentGammaBack = getNextPaymentGammaRandomSampleGenerator();

        nextOrderGamma.setPayment(nextPaymentGammaBack);
        assertThat(nextOrderGamma.getPayment()).isEqualTo(nextPaymentGammaBack);

        nextOrderGamma.payment(null);
        assertThat(nextOrderGamma.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderGamma nextOrderGamma = getNextOrderGammaRandomSampleGenerator();
        NextShipmentGamma nextShipmentGammaBack = getNextShipmentGammaRandomSampleGenerator();

        nextOrderGamma.setShipment(nextShipmentGammaBack);
        assertThat(nextOrderGamma.getShipment()).isEqualTo(nextShipmentGammaBack);

        nextOrderGamma.shipment(null);
        assertThat(nextOrderGamma.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderGamma nextOrderGamma = getNextOrderGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderGamma.setTenant(masterTenantBack);
        assertThat(nextOrderGamma.getTenant()).isEqualTo(masterTenantBack);

        nextOrderGamma.tenant(null);
        assertThat(nextOrderGamma.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderGamma nextOrderGamma = getNextOrderGammaRandomSampleGenerator();
        NextCustomerGamma nextCustomerGammaBack = getNextCustomerGammaRandomSampleGenerator();

        nextOrderGamma.setCustomer(nextCustomerGammaBack);
        assertThat(nextOrderGamma.getCustomer()).isEqualTo(nextCustomerGammaBack);

        nextOrderGamma.customer(null);
        assertThat(nextOrderGamma.getCustomer()).isNull();
    }
}
