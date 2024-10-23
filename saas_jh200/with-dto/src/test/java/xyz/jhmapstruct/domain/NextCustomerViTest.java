package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerViTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerVi.class);
        NextCustomerVi nextCustomerVi1 = getNextCustomerViSample1();
        NextCustomerVi nextCustomerVi2 = new NextCustomerVi();
        assertThat(nextCustomerVi1).isNotEqualTo(nextCustomerVi2);

        nextCustomerVi2.setId(nextCustomerVi1.getId());
        assertThat(nextCustomerVi1).isEqualTo(nextCustomerVi2);

        nextCustomerVi2 = getNextCustomerViSample2();
        assertThat(nextCustomerVi1).isNotEqualTo(nextCustomerVi2);
    }

    @Test
    void ordersTest() {
        NextCustomerVi nextCustomerVi = getNextCustomerViRandomSampleGenerator();
        NextOrderVi nextOrderViBack = getNextOrderViRandomSampleGenerator();

        nextCustomerVi.addOrders(nextOrderViBack);
        assertThat(nextCustomerVi.getOrders()).containsOnly(nextOrderViBack);
        assertThat(nextOrderViBack.getCustomer()).isEqualTo(nextCustomerVi);

        nextCustomerVi.removeOrders(nextOrderViBack);
        assertThat(nextCustomerVi.getOrders()).doesNotContain(nextOrderViBack);
        assertThat(nextOrderViBack.getCustomer()).isNull();

        nextCustomerVi.orders(new HashSet<>(Set.of(nextOrderViBack)));
        assertThat(nextCustomerVi.getOrders()).containsOnly(nextOrderViBack);
        assertThat(nextOrderViBack.getCustomer()).isEqualTo(nextCustomerVi);

        nextCustomerVi.setOrders(new HashSet<>());
        assertThat(nextCustomerVi.getOrders()).doesNotContain(nextOrderViBack);
        assertThat(nextOrderViBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerVi nextCustomerVi = getNextCustomerViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerVi.setTenant(masterTenantBack);
        assertThat(nextCustomerVi.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerVi.tenant(null);
        assertThat(nextCustomerVi.getTenant()).isNull();
    }
}
