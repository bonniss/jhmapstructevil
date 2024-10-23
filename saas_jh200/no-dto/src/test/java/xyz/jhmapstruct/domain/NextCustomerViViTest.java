package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderViViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerViVi.class);
        NextCustomerViVi nextCustomerViVi1 = getNextCustomerViViSample1();
        NextCustomerViVi nextCustomerViVi2 = new NextCustomerViVi();
        assertThat(nextCustomerViVi1).isNotEqualTo(nextCustomerViVi2);

        nextCustomerViVi2.setId(nextCustomerViVi1.getId());
        assertThat(nextCustomerViVi1).isEqualTo(nextCustomerViVi2);

        nextCustomerViVi2 = getNextCustomerViViSample2();
        assertThat(nextCustomerViVi1).isNotEqualTo(nextCustomerViVi2);
    }

    @Test
    void ordersTest() {
        NextCustomerViVi nextCustomerViVi = getNextCustomerViViRandomSampleGenerator();
        NextOrderViVi nextOrderViViBack = getNextOrderViViRandomSampleGenerator();

        nextCustomerViVi.addOrders(nextOrderViViBack);
        assertThat(nextCustomerViVi.getOrders()).containsOnly(nextOrderViViBack);
        assertThat(nextOrderViViBack.getCustomer()).isEqualTo(nextCustomerViVi);

        nextCustomerViVi.removeOrders(nextOrderViViBack);
        assertThat(nextCustomerViVi.getOrders()).doesNotContain(nextOrderViViBack);
        assertThat(nextOrderViViBack.getCustomer()).isNull();

        nextCustomerViVi.orders(new HashSet<>(Set.of(nextOrderViViBack)));
        assertThat(nextCustomerViVi.getOrders()).containsOnly(nextOrderViViBack);
        assertThat(nextOrderViViBack.getCustomer()).isEqualTo(nextCustomerViVi);

        nextCustomerViVi.setOrders(new HashSet<>());
        assertThat(nextCustomerViVi.getOrders()).doesNotContain(nextOrderViViBack);
        assertThat(nextOrderViViBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerViVi nextCustomerViVi = getNextCustomerViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerViVi.setTenant(masterTenantBack);
        assertThat(nextCustomerViVi.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerViVi.tenant(null);
        assertThat(nextCustomerViVi.getTenant()).isNull();
    }
}
