package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderSigmaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerSigma.class);
        NextCustomerSigma nextCustomerSigma1 = getNextCustomerSigmaSample1();
        NextCustomerSigma nextCustomerSigma2 = new NextCustomerSigma();
        assertThat(nextCustomerSigma1).isNotEqualTo(nextCustomerSigma2);

        nextCustomerSigma2.setId(nextCustomerSigma1.getId());
        assertThat(nextCustomerSigma1).isEqualTo(nextCustomerSigma2);

        nextCustomerSigma2 = getNextCustomerSigmaSample2();
        assertThat(nextCustomerSigma1).isNotEqualTo(nextCustomerSigma2);
    }

    @Test
    void ordersTest() {
        NextCustomerSigma nextCustomerSigma = getNextCustomerSigmaRandomSampleGenerator();
        NextOrderSigma nextOrderSigmaBack = getNextOrderSigmaRandomSampleGenerator();

        nextCustomerSigma.addOrders(nextOrderSigmaBack);
        assertThat(nextCustomerSigma.getOrders()).containsOnly(nextOrderSigmaBack);
        assertThat(nextOrderSigmaBack.getCustomer()).isEqualTo(nextCustomerSigma);

        nextCustomerSigma.removeOrders(nextOrderSigmaBack);
        assertThat(nextCustomerSigma.getOrders()).doesNotContain(nextOrderSigmaBack);
        assertThat(nextOrderSigmaBack.getCustomer()).isNull();

        nextCustomerSigma.orders(new HashSet<>(Set.of(nextOrderSigmaBack)));
        assertThat(nextCustomerSigma.getOrders()).containsOnly(nextOrderSigmaBack);
        assertThat(nextOrderSigmaBack.getCustomer()).isEqualTo(nextCustomerSigma);

        nextCustomerSigma.setOrders(new HashSet<>());
        assertThat(nextCustomerSigma.getOrders()).doesNotContain(nextOrderSigmaBack);
        assertThat(nextOrderSigmaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerSigma nextCustomerSigma = getNextCustomerSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerSigma.setTenant(masterTenantBack);
        assertThat(nextCustomerSigma.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerSigma.tenant(null);
        assertThat(nextCustomerSigma.getTenant()).isNull();
    }
}
