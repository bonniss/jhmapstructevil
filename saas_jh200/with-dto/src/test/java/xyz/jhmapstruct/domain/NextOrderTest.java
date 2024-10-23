package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderTestSamples.*;
import static xyz.jhmapstruct.domain.NextPaymentTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductTestSamples.*;
import static xyz.jhmapstruct.domain.NextShipmentTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrder.class);
        NextOrder nextOrder1 = getNextOrderSample1();
        NextOrder nextOrder2 = new NextOrder();
        assertThat(nextOrder1).isNotEqualTo(nextOrder2);

        nextOrder2.setId(nextOrder1.getId());
        assertThat(nextOrder1).isEqualTo(nextOrder2);

        nextOrder2 = getNextOrderSample2();
        assertThat(nextOrder1).isNotEqualTo(nextOrder2);
    }

    @Test
    void productsTest() {
        NextOrder nextOrder = getNextOrderRandomSampleGenerator();
        NextProduct nextProductBack = getNextProductRandomSampleGenerator();

        nextOrder.addProducts(nextProductBack);
        assertThat(nextOrder.getProducts()).containsOnly(nextProductBack);
        assertThat(nextProductBack.getOrder()).isEqualTo(nextOrder);

        nextOrder.removeProducts(nextProductBack);
        assertThat(nextOrder.getProducts()).doesNotContain(nextProductBack);
        assertThat(nextProductBack.getOrder()).isNull();

        nextOrder.products(new HashSet<>(Set.of(nextProductBack)));
        assertThat(nextOrder.getProducts()).containsOnly(nextProductBack);
        assertThat(nextProductBack.getOrder()).isEqualTo(nextOrder);

        nextOrder.setProducts(new HashSet<>());
        assertThat(nextOrder.getProducts()).doesNotContain(nextProductBack);
        assertThat(nextProductBack.getOrder()).isNull();
    }

    @Test
    void paymentTest() {
        NextOrder nextOrder = getNextOrderRandomSampleGenerator();
        NextPayment nextPaymentBack = getNextPaymentRandomSampleGenerator();

        nextOrder.setPayment(nextPaymentBack);
        assertThat(nextOrder.getPayment()).isEqualTo(nextPaymentBack);

        nextOrder.payment(null);
        assertThat(nextOrder.getPayment()).isNull();
    }

    @Test
    void shipmentTest() {
        NextOrder nextOrder = getNextOrderRandomSampleGenerator();
        NextShipment nextShipmentBack = getNextShipmentRandomSampleGenerator();

        nextOrder.setShipment(nextShipmentBack);
        assertThat(nextOrder.getShipment()).isEqualTo(nextShipmentBack);

        nextOrder.shipment(null);
        assertThat(nextOrder.getShipment()).isNull();
    }

    @Test
    void tenantTest() {
        NextOrder nextOrder = getNextOrderRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextOrder.setTenant(masterTenantBack);
        assertThat(nextOrder.getTenant()).isEqualTo(masterTenantBack);

        nextOrder.tenant(null);
        assertThat(nextOrder.getTenant()).isNull();
    }

    @Test
    void customerTest() {
        NextOrder nextOrder = getNextOrderRandomSampleGenerator();
        NextCustomer nextCustomerBack = getNextCustomerRandomSampleGenerator();

        nextOrder.setCustomer(nextCustomerBack);
        assertThat(nextOrder.getCustomer()).isEqualTo(nextCustomerBack);

        nextOrder.customer(null);
        assertThat(nextOrder.getCustomer()).isNull();
    }
}
