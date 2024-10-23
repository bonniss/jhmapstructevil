package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CustomerMiTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductMiTestSamples.*;
import static xyz.jhmapstruct.domain.PaymentMiTestSamples.*;
import static xyz.jhmapstruct.domain.ShipmentMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderMi.class);
        NextOrderMi nextOrderMi1 = getNextOrderMiSample1();
        NextOrderMi nextOrderMi2 = new NextOrderMi();
        assertThat(nextOrderMi1).isNotEqualTo(nextOrderMi2);

        nextOrderMi2.setId(nextOrderMi1.getId());
        assertThat(nextOrderMi1).isEqualTo(nextOrderMi2);

        nextOrderMi2 = getNextOrderMiSample2();
        assertThat(nextOrderMi1).isNotEqualTo(nextOrderMi2);
    }

    @Test
    void productsTest() {
        NextOrderMi nextOrderMi = getNextOrderMiRandomSampleGenerator();
        NextProductMi nextProductMiBack = getNextProductMiRandomSampleGenerator();

        nextOrderMi.addProducts(nextProductMiBack);
        assertThat(nextOrderMi.getProducts()).containsOnly(nextProductMiBack);
        assertThat(nextProductMiBack.getOrder()).isEqualTo(nextOrderMi);

        nextOrderMi.removeProducts(nextProductMiBack);
        assertThat(nextOrderMi.getProducts()).doesNotContain(nextProductMiBack);
        assertThat(nextProductMiBack.getOrder()).isNull();

        nextOrderMi.products(new HashSet<>(Set.of(nextProductMiBack)));
        assertThat(nextOrderMi.getProducts()).containsOnly(nextProductMiBack);
        assertThat(nextProductMiBack.getOrder()).isEqualTo(nextOrderMi);

        nextOrderMi.setProducts(new HashSet<>());
        assertThat(nextOrderMi.getProducts()).doesNotContain(nextProductMiBack);
        assertThat(nextProductMiBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderMi nextOrderMi = getNextOrderMiRandomSampleGenerator();
        PaymentMi paymentMiBack = getPaymentMiRandomSampleGenerator();

        nextOrderMi.setPayment(paymentMiBack);
        assertThat(nextOrderMi.getPayment()).isEqualTo(paymentMiBack);

        nextOrderMi.payment(null);
        assertThat(nextOrderMi.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderMi nextOrderMi = getNextOrderMiRandomSampleGenerator();
        ShipmentMi shipmentMiBack = getShipmentMiRandomSampleGenerator();

        nextOrderMi.setShipment(shipmentMiBack);
        assertThat(nextOrderMi.getShipment()).isEqualTo(shipmentMiBack);

        nextOrderMi.shipment(null);
        assertThat(nextOrderMi.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderMi nextOrderMi = getNextOrderMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderMi.setTenant(masterTenantBack);
        assertThat(nextOrderMi.getTenant()).isEqualTo(masterTenantBack);

        nextOrderMi.tenant(null);
        assertThat(nextOrderMi.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderMi nextOrderMi = getNextOrderMiRandomSampleGenerator();
        CustomerMi customerMiBack = getCustomerMiRandomSampleGenerator();

        nextOrderMi.setCustomer(customerMiBack);
        assertThat(nextOrderMi.getCustomer()).isEqualTo(customerMiBack);

        nextOrderMi.customer(null);
        assertThat(nextOrderMi.getCustomer()).isNull();
    }
}
