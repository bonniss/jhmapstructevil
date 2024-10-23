package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCustomerGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderGammaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerGamma.class);
        NextCustomerGamma nextCustomerGamma1 = getNextCustomerGammaSample1();
        NextCustomerGamma nextCustomerGamma2 = new NextCustomerGamma();
        assertThat(nextCustomerGamma1).isNotEqualTo(nextCustomerGamma2);

        nextCustomerGamma2.setId(nextCustomerGamma1.getId());
        assertThat(nextCustomerGamma1).isEqualTo(nextCustomerGamma2);

        nextCustomerGamma2 = getNextCustomerGammaSample2();
        assertThat(nextCustomerGamma1).isNotEqualTo(nextCustomerGamma2);
    }

    @Test
    void ordersTest() {
        NextCustomerGamma nextCustomerGamma = getNextCustomerGammaRandomSampleGenerator();
        NextOrderGamma nextOrderGammaBack = getNextOrderGammaRandomSampleGenerator();

        nextCustomerGamma.addOrders(nextOrderGammaBack);
        assertThat(nextCustomerGamma.getOrders()).containsOnly(nextOrderGammaBack);
        assertThat(nextOrderGammaBack.getCustomer()).isEqualTo(nextCustomerGamma);

        nextCustomerGamma.removeOrders(nextOrderGammaBack);
        assertThat(nextCustomerGamma.getOrders()).doesNotContain(nextOrderGammaBack);
        assertThat(nextOrderGammaBack.getCustomer()).isNull();

        nextCustomerGamma.orders(new HashSet<>(Set.of(nextOrderGammaBack)));
        assertThat(nextCustomerGamma.getOrders()).containsOnly(nextOrderGammaBack);
        assertThat(nextOrderGammaBack.getCustomer()).isEqualTo(nextCustomerGamma);

        nextCustomerGamma.setOrders(new HashSet<>());
        assertThat(nextCustomerGamma.getOrders()).doesNotContain(nextOrderGammaBack);
        assertThat(nextOrderGammaBack.getCustomer()).isNull();
    }

    @Test
    void tenantTest() {
        NextCustomerGamma nextCustomerGamma = getNextCustomerGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextCustomerGamma.setTenant(masterTenantBack);
        assertThat(nextCustomerGamma.getTenant()).isEqualTo(masterTenantBack);

        nextCustomerGamma.tenant(null);
        assertThat(nextCustomerGamma.getTenant()).isNull();
    }
}
