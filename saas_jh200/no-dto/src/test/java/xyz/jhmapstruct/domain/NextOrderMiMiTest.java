package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentMiMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderMiMi.class);
        NextOrderMiMi nextOrderMiMi1 = getNextOrderMiMiSample1();
        NextOrderMiMi nextOrderMiMi2 = new NextOrderMiMi();
        assertThat(nextOrderMiMi1).isNotEqualTo(nextOrderMiMi2);

        nextOrderMiMi2.setId(nextOrderMiMi1.getId());
        assertThat(nextOrderMiMi1).isEqualTo(nextOrderMiMi2);

        nextOrderMiMi2 = getNextOrderMiMiSample2();
        assertThat(nextOrderMiMi1).isNotEqualTo(nextOrderMiMi2);
    }

    @Test
    void productsTest() {
        NextOrderMiMi nextOrderMiMi = getNextOrderMiMiRandomSampleGenerator();
        NextProductMiMi nextProductMiMiBack = getNextProductMiMiRandomSampleGenerator();

        nextOrderMiMi.addProducts(nextProductMiMiBack);
        assertThat(nextOrderMiMi.getProducts()).containsOnly(nextProductMiMiBack);
        assertThat(nextProductMiMiBack.getOrder()).isEqualTo(nextOrderMiMi);

        nextOrderMiMi.removeProducts(nextProductMiMiBack);
        assertThat(nextOrderMiMi.getProducts()).doesNotContain(nextProductMiMiBack);
        assertThat(nextProductMiMiBack.getOrder()).isNull();

        nextOrderMiMi.products(new HashSet<>(Set.of(nextProductMiMiBack)));
        assertThat(nextOrderMiMi.getProducts()).containsOnly(nextProductMiMiBack);
        assertThat(nextProductMiMiBack.getOrder()).isEqualTo(nextOrderMiMi);

        nextOrderMiMi.setProducts(new HashSet<>());
        assertThat(nextOrderMiMi.getProducts()).doesNotContain(nextProductMiMiBack);
        assertThat(nextProductMiMiBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrderMiMi nextOrderMiMi = getNextOrderMiMiRandomSampleGenerator();
        NextPaymentMiMi nextPaymentMiMiBack = getNextPaymentMiMiRandomSampleGenerator();

        nextOrderMiMi.setPayment(nextPaymentMiMiBack);
        assertThat(nextOrderMiMi.getPayment()).isEqualTo(nextPaymentMiMiBack);

        nextOrderMiMi.payment(null);
        assertThat(nextOrderMiMi.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrderMiMi nextOrderMiMi = getNextOrderMiMiRandomSampleGenerator();
        NextShipmentMiMi nextShipmentMiMiBack = getNextShipmentMiMiRandomSampleGenerator();

        nextOrderMiMi.setShipment(nextShipmentMiMiBack);
        assertThat(nextOrderMiMi.getShipment()).isEqualTo(nextShipmentMiMiBack);

        nextOrderMiMi.shipment(null);
        assertThat(nextOrderMiMi.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrderMiMi nextOrderMiMi = getNextOrderMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrderMiMi.setTenant(masterTenantBack);
        assertThat(nextOrderMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextOrderMiMi.tenant(null);
        assertThat(nextOrderMiMi.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrderMiMi nextOrderMiMi = getNextOrderMiMiRandomSampleGenerator();
        NextCustomerMiMi nextCustomerMiMiBack = getNextCustomerMiMiRandomSampleGenerator();

        nextOrderMiMi.setCustomer(nextCustomerMiMiBack);
        assertThat(nextOrderMiMi.getCustomer()).isEqualTo(nextCustomerMiMiBack);

        nextOrderMiMi.customer(null);
        assertThat(nextOrderMiMi.getCustomer()).isNull();
    }
}
