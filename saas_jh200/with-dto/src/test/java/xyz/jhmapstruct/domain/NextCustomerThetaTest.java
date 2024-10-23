package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderThetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerTheta.class);
        NextCustomerTheta nextCustomerTheta1 = getNextCustomerThetaSample1();
        NextCustomerTheta nextCustomerTheta2 = new NextCustomerTheta();
        assertThat(nextCustomerTheta1).isNotEqualTo(nextCustomerTheta2);

        nextCustomerTheta2.setId(nextCustomerTheta1.getId());
        assertThat(nextCustomerTheta1).isEqualTo(nextCustomerTheta2);

        nextCustomerTheta2 = getNextCustomerThetaSample2();
        assertThat(nextCustomerTheta1).isNotEqualTo(nextCustomerTheta2);
    }

    @Test
    void ordersTest() {
        NextCustomerTheta nextCustomerTheta = getNextCustomerThetaRandomSampleGenerator();
        NextOrderTheta nextOrderThetaBack = getNextOrderThetaRandomSampleGenerator();

        nextCustomerTheta.addOrders(nextOrderThetaBack);
        assertThat(nextCustomerTheta.getOrders()).containsOnly(nextOrderThetaBack);
        assertThat(nextOrderThetaBack.getCustomer()).isEqualTo(nextCustomerTheta);

        nextCustomerTheta.removeOrders(nextOrderThetaBack);
        assertThat(nextCustomerTheta.getOrders()).doesNotContain(nextOrderThetaBack);
        assertThat(nextOrderThetaBack.getCustomer()).isNull();

        nextCustomerTheta.orders(new HashSet<>(Set.of(nextOrderThetaBack)));
        assertThat(nextCustomerTheta.getOrders()).containsOnly(nextOrderThetaBack);
        assertThat(nextOrderThetaBack.getCustomer()).isEqualTo(nextCustomerTheta);

        nextCustomerTheta.setOrders(new HashSet<>());
        assertThat(nextCustomerTheta.getOrders()).doesNotContain(nextOrderThetaBack);
        assertThat(nextOrderThetaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerTheta nextCustomerTheta = getNextCustomerThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerTheta.setTenant(masterTenantBack);
        assertThat(nextCustomerTheta.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerTheta.tenant(null);
        assertThat(nextCustomerTheta.getTenant()).isNull();
    }
}
