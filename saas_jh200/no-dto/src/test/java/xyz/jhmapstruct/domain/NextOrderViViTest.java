package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentViViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderViVi.class);
        NextOrderViVi nextOrderViVi1 = getNextOrderViViSample1();
        NextOrderViVi nextOrderViVi2 = new NextOrderViVi();
        assertThat(nextOrderViVi1).isNotEqualTo(nextOrderViVi2);

        nextOrderViVi2.setId(nextOrderViVi1.getId());
        assertThat(nextOrderViVi1).isEqualTo(nextOrderViVi2);

        nextOrderViVi2 = getNextOrderViViSample2();
        assertThat(nextOrderViVi1).isNotEqualTo(nextOrderViVi2);
    }

    @Test
    void productsTest() {
        NextOrderViVi nextOrderViVi = getNextOrderViViRandomSampleGenerator();
        NextProductViVi nextProductViViBack = getNextProductViViRandomSampleGenerator();

        nextOrderViVi.addProducts(nextProductViViBack);
        assertThat(nextOrderViVi.getProducts()).containsOnly(nextProductViViBack);
        assertThat(nextProductViViBack.getOrder()).isEqualTo(nextOrderViVi);

        nextOrderViVi.removeProducts(nextProductViViBack);
        assertThat(nextOrderViVi.getProducts()).doesNotContain(nextProductViViBack);
        assertThat(nextProductViViBack.getOrder()).isNull();

        nextOrderViVi.products(new HashSet<>(Set.of(nextProductViViBack)));
        assertThat(nextOrderViVi.getProducts()).containsOnly(nextProductViViBack);
        assertThat(nextProductViViBack.getOrder()).isEqualTo(nextOrderViVi);

        nextOrderViVi.setProducts(new HashSet<>());
        assertThat(nextOrderViVi.getProducts()).doesNotContain(nextProductViViBack);
        assertThat(nextProductViViBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderViVi nextOrderViVi = getNextOrderViViRandomSampleGenerator();
        NextPaymentViVi nextPaymentViViBack = getNextPaymentViViRandomSampleGenerator();

        nextOrderViVi.setPayment(nextPaymentViViBack);
        assertThat(nextOrderViVi.getPayment()).isEqualTo(nextPaymentViViBack);

        nextOrderViVi.payment(null);
        assertThat(nextOrderViVi.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderViVi nextOrderViVi = getNextOrderViViRandomSampleGenerator();
        NextShipmentViVi nextShipmentViViBack = getNextShipmentViViRandomSampleGenerator();

        nextOrderViVi.setShipment(nextShipmentViViBack);
        assertThat(nextOrderViVi.getShipment()).isEqualTo(nextShipmentViViBack);

        nextOrderViVi.shipment(null);
        assertThat(nextOrderViVi.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderViVi nextOrderViVi = getNextOrderViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderViVi.setTenant(masterTenantBack);
        assertThat(nextOrderViVi.getTenant()).isEqualTo(masterTenantBack);

        nextOrderViVi.tenant(null);
        assertThat(nextOrderViVi.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderViVi nextOrderViVi = getNextOrderViViRandomSampleGenerator();
        NextCustomerViVi nextCustomerViViBack = getNextCustomerViViRandomSampleGenerator();

        nextOrderViVi.setCustomer(nextCustomerViViBack);
        assertThat(nextOrderViVi.getCustomer()).isEqualTo(nextCustomerViViBack);

        nextOrderViVi.customer(null);
        assertThat(nextOrderViVi.getCustomer()).isNull();
    }
}
