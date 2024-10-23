package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerViTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderViTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentViTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductViTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderVi.class);
        NextOrderVi nextOrderVi1 = getNextOrderViSample1();
        NextOrderVi nextOrderVi2 = new NextOrderVi();
        assertThat(nextOrderVi1).isNotEqualTo(nextOrderVi2);

        nextOrderVi2.setId(nextOrderVi1.getId());
        assertThat(nextOrderVi1).isEqualTo(nextOrderVi2);

        nextOrderVi2 = getNextOrderViSample2();
        assertThat(nextOrderVi1).isNotEqualTo(nextOrderVi2);
    }

    @Test
    void productsTest() {
        NextOrderVi nextOrderVi = getNextOrderViRandomSampleGenerator();
        NextProductVi nextProductViBack = getNextProductViRandomSampleGenerator();

        nextOrderVi.addProducts(nextProductViBack);
        assertThat(nextOrderVi.getProducts()).containsOnly(nextProductViBack);
        assertThat(nextProductViBack.getOrder()).isEqualTo(nextOrderVi);

        nextOrderVi.removeProducts(nextProductViBack);
        assertThat(nextOrderVi.getProducts()).doesNotContain(nextProductViBack);
        assertThat(nextProductViBack.getOrder()).isNull();

        nextOrderVi.products(new HashSet<>(Set.of(nextProductViBack)));
        assertThat(nextOrderVi.getProducts()).containsOnly(nextProductViBack);
        assertThat(nextProductViBack.getOrder()).isEqualTo(nextOrderVi);

        nextOrderVi.setProducts(new HashSet<>());
        assertThat(nextOrderVi.getProducts()).doesNotContain(nextProductViBack);
        assertThat(nextProductViBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderVi nextOrderVi = getNextOrderViRandomSampleGenerator();
        NextPaymentVi nextPaymentViBack = getNextPaymentViRandomSampleGenerator();

        nextOrderVi.setPayment(nextPaymentViBack);
        assertThat(nextOrderVi.getPayment()).isEqualTo(nextPaymentViBack);

        nextOrderVi.payment(null);
        assertThat(nextOrderVi.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderVi nextOrderVi = getNextOrderViRandomSampleGenerator();
        NextShipmentVi nextShipmentViBack = getNextShipmentViRandomSampleGenerator();

        nextOrderVi.setShipment(nextShipmentViBack);
        assertThat(nextOrderVi.getShipment()).isEqualTo(nextShipmentViBack);

        nextOrderVi.shipment(null);
        assertThat(nextOrderVi.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderVi nextOrderVi = getNextOrderViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderVi.setTenant(masterTenantBack);
        assertThat(nextOrderVi.getTenant()).isEqualTo(masterTenantBack);

        nextOrderVi.tenant(null);
        assertThat(nextOrderVi.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderVi nextOrderVi = getNextOrderViRandomSampleGenerator();
        NextCustomerVi nextCustomerViBack = getNextCustomerViRandomSampleGenerator();

        nextOrderVi.setCustomer(nextCustomerViBack);
        assertThat(nextOrderVi.getCustomer()).isEqualTo(nextCustomerViBack);

        nextOrderVi.customer(null);
        assertThat(nextOrderVi.getCustomer()).isNull();
    }
}
