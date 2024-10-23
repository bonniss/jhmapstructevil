package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentThetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderTheta.class);
        NextOrderTheta nextOrderTheta1 = getNextOrderThetaSample1();
        NextOrderTheta nextOrderTheta2 = new NextOrderTheta();
        assertThat(nextOrderTheta1).isNotEqualTo(nextOrderTheta2);

        nextOrderTheta2.setId(nextOrderTheta1.getId());
        assertThat(nextOrderTheta1).isEqualTo(nextOrderTheta2);

        nextOrderTheta2 = getNextOrderThetaSample2();
        assertThat(nextOrderTheta1).isNotEqualTo(nextOrderTheta2);
    }

    @Test
    void productsTest() {
        NextOrderTheta nextOrderTheta = getNextOrderThetaRandomSampleGenerator();
        NextProductTheta nextProductThetaBack = getNextProductThetaRandomSampleGenerator();

        nextOrderTheta.addProducts(nextProductThetaBack);
        assertThat(nextOrderTheta.getProducts()).containsOnly(nextProductThetaBack);
        assertThat(nextProductThetaBack.getOrder()).isEqualTo(nextOrderTheta);

        nextOrderTheta.removeProducts(nextProductThetaBack);
        assertThat(nextOrderTheta.getProducts()).doesNotContain(nextProductThetaBack);
        assertThat(nextProductThetaBack.getOrder()).isNull();

        nextOrderTheta.products(new HashSet<>(Set.of(nextProductThetaBack)));
        assertThat(nextOrderTheta.getProducts()).containsOnly(nextProductThetaBack);
        assertThat(nextProductThetaBack.getOrder()).isEqualTo(nextOrderTheta);

        nextOrderTheta.setProducts(new HashSet<>());
        assertThat(nextOrderTheta.getProducts()).doesNotContain(nextProductThetaBack);
        assertThat(nextProductThetaBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderTheta nextOrderTheta = getNextOrderThetaRandomSampleGenerator();
        NextPaymentTheta nextPaymentThetaBack = getNextPaymentThetaRandomSampleGenerator();

        nextOrderTheta.setPayment(nextPaymentThetaBack);
        assertThat(nextOrderTheta.getPayment()).isEqualTo(nextPaymentThetaBack);

        nextOrderTheta.payment(null);
        assertThat(nextOrderTheta.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderTheta nextOrderTheta = getNextOrderThetaRandomSampleGenerator();
        NextShipmentTheta nextShipmentThetaBack = getNextShipmentThetaRandomSampleGenerator();

        nextOrderTheta.setShipment(nextShipmentThetaBack);
        assertThat(nextOrderTheta.getShipment()).isEqualTo(nextShipmentThetaBack);

        nextOrderTheta.shipment(null);
        assertThat(nextOrderTheta.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderTheta nextOrderTheta = getNextOrderThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderTheta.setTenant(masterTenantBack);
        assertThat(nextOrderTheta.getTenant()).isEqualTo(masterTenantBack);

        nextOrderTheta.tenant(null);
        assertThat(nextOrderTheta.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderTheta nextOrderTheta = getNextOrderThetaRandomSampleGenerator();
        NextCustomerTheta nextCustomerThetaBack = getNextCustomerThetaRandomSampleGenerator();

        nextOrderTheta.setCustomer(nextCustomerThetaBack);
        assertThat(nextOrderTheta.getCustomer()).isEqualTo(nextCustomerThetaBack);

        nextOrderTheta.customer(null);
        assertThat(nextOrderTheta.getCustomer()).isNull();
    }
}
