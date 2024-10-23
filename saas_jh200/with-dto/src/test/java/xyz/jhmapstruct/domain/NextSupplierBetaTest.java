package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierBetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierBeta.class);
        NextSupplierBeta nextSupplierBeta1 = getNextSupplierBetaSample1();
        NextSupplierBeta nextSupplierBeta2 = new NextSupplierBeta();
        assertThat(nextSupplierBeta1).isNotEqualTo(nextSupplierBeta2);

        nextSupplierBeta2.setId(nextSupplierBeta1.getId());
        assertThat(nextSupplierBeta1).isEqualTo(nextSupplierBeta2);

        nextSupplierBeta2 = getNextSupplierBetaSample2();
        assertThat(nextSupplierBeta1).isNotEqualTo(nextSupplierBeta2);
    }

    @Test
    void tenantTest() {
        NextSupplierBeta nextSupplierBeta = getNextSupplierBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierBeta.setTenant(masterTenantBack);
        assertThat(nextSupplierBeta.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierBeta.tenant(null);
        assertThat(nextSupplierBeta.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierBeta nextSupplierBeta = getNextSupplierBetaRandomSampleGenerator();
        NextProductBeta nextProductBetaBack = getNextProductBetaRandomSampleGenerator();

        nextSupplierBeta.addProducts(nextProductBetaBack);
        assertThat(nextSupplierBeta.getProducts()).containsOnly(nextProductBetaBack);

        nextSupplierBeta.removeProducts(nextProductBetaBack);
        assertThat(nextSupplierBeta.getProducts()).doesNotContain(nextProductBetaBack);

        nextSupplierBeta.products(new HashSet<>(Set.of(nextProductBetaBack)));
        assertThat(nextSupplierBeta.getProducts()).containsOnly(nextProductBetaBack);

        nextSupplierBeta.setProducts(new HashSet<>());
        assertThat(nextSupplierBeta.getProducts()).doesNotContain(nextProductBetaBack);
    }
}
