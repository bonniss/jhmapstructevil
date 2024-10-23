package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderBetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerBeta.class);
        NextCustomerBeta nextCustomerBeta1 = getNextCustomerBetaSample1();
        NextCustomerBeta nextCustomerBeta2 = new NextCustomerBeta();
        assertThat(nextCustomerBeta1).isNotEqualTo(nextCustomerBeta2);

        nextCustomerBeta2.setId(nextCustomerBeta1.getId());
        assertThat(nextCustomerBeta1).isEqualTo(nextCustomerBeta2);

        nextCustomerBeta2 = getNextCustomerBetaSample2();
        assertThat(nextCustomerBeta1).isNotEqualTo(nextCustomerBeta2);
    }

    @Test
    void ordersTest() {
        NextCustomerBeta nextCustomerBeta = getNextCustomerBetaRandomSampleGenerator();
        NextOrderBeta nextOrderBetaBack = getNextOrderBetaRandomSampleGenerator();

        nextCustomerBeta.addOrders(nextOrderBetaBack);
        assertThat(nextCustomerBeta.getOrders()).containsOnly(nextOrderBetaBack);
        assertThat(nextOrderBetaBack.getCustomer()).isEqualTo(nextCustomerBeta);

        nextCustomerBeta.removeOrders(nextOrderBetaBack);
        assertThat(nextCustomerBeta.getOrders()).doesNotContain(nextOrderBetaBack);
        assertThat(nextOrderBetaBack.getCustomer()).isNull();

        nextCustomerBeta.orders(new HashSet<>(Set.of(nextOrderBetaBack)));
        assertThat(nextCustomerBeta.getOrders()).containsOnly(nextOrderBetaBack);
        assertThat(nextOrderBetaBack.getCustomer()).isEqualTo(nextCustomerBeta);

        nextCustomerBeta.setOrders(new HashSet<>());
        assertThat(nextCustomerBeta.getOrders()).doesNotContain(nextOrderBetaBack);
        assertThat(nextOrderBetaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerBeta nextCustomerBeta = getNextCustomerBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerBeta.setTenant(masterTenantBack);
        assertThat(nextCustomerBeta.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerBeta.tenant(null);
        assertThat(nextCustomerBeta.getTenant()).isNull();
    }
}
