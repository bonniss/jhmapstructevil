package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderAlphaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerAlpha.class);
        NextCustomerAlpha nextCustomerAlpha1 = getNextCustomerAlphaSample1();
        NextCustomerAlpha nextCustomerAlpha2 = new NextCustomerAlpha();
        assertThat(nextCustomerAlpha1).isNotEqualTo(nextCustomerAlpha2);

        nextCustomerAlpha2.setId(nextCustomerAlpha1.getId());
        assertThat(nextCustomerAlpha1).isEqualTo(nextCustomerAlpha2);

        nextCustomerAlpha2 = getNextCustomerAlphaSample2();
        assertThat(nextCustomerAlpha1).isNotEqualTo(nextCustomerAlpha2);
    }

    @Test
    void ordersTest() {
        NextCustomerAlpha nextCustomerAlpha = getNextCustomerAlphaRandomSampleGenerator();
        NextOrderAlpha nextOrderAlphaBack = getNextOrderAlphaRandomSampleGenerator();

        nextCustomerAlpha.addOrders(nextOrderAlphaBack);
        assertThat(nextCustomerAlpha.getOrders()).containsOnly(nextOrderAlphaBack);
        assertThat(nextOrderAlphaBack.getCustomer()).isEqualTo(nextCustomerAlpha);

        nextCustomerAlpha.removeOrders(nextOrderAlphaBack);
        assertThat(nextCustomerAlpha.getOrders()).doesNotContain(nextOrderAlphaBack);
        assertThat(nextOrderAlphaBack.getCustomer()).isNull();

        nextCustomerAlpha.orders(new HashSet<>(Set.of(nextOrderAlphaBack)));
        assertThat(nextCustomerAlpha.getOrders()).containsOnly(nextOrderAlphaBack);
        assertThat(nextOrderAlphaBack.getCustomer()).isEqualTo(nextCustomerAlpha);

        nextCustomerAlpha.setOrders(new HashSet<>());
        assertThat(nextCustomerAlpha.getOrders()).doesNotContain(nextOrderAlphaBack);
        assertThat(nextOrderAlphaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerAlpha nextCustomerAlpha = getNextCustomerAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerAlpha.setTenant(masterTenantBack);
        assertThat(nextCustomerAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerAlpha.tenant(null);
        assertThat(nextCustomerAlpha.getTenant()).isNull();
    }
}
