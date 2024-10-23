package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductBetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierBetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductBeta.class);
        NextProductBeta nextProductBeta1 = getNextProductBetaSample1();
        NextProductBeta nextProductBeta2 = new NextProductBeta();
        assertThat(nextProductBeta1).isNotEqualTo(nextProductBeta2);

        nextProductBeta2.setId(nextProductBeta1.getId());
        assertThat(nextProductBeta1).isEqualTo(nextProductBeta2);

        nextProductBeta2 = getNextProductBetaSample2();
        assertThat(nextProductBeta1).isNotEqualTo(nextProductBeta2);
    }

    @Test
    void categoryTest() {
        NextProductBeta nextProductBeta = getNextProductBetaRandomSampleGenerator();
        NextCategoryBeta nextCategoryBetaBack = getNextCategoryBetaRandomSampleGenerator();

        nextProductBeta.setCategory(nextCategoryBetaBack);
        assertThat(nextProductBeta.getCategory()).isEqualTo(nextCategoryBetaBack);

        nextProductBeta.category(null);
        assertThat(nextProductBeta.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductBeta nextProductBeta = getNextProductBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductBeta.setTenant(masterTenantBack);
        assertThat(nextProductBeta.getTenant()).isEqualTo(masterTenantBack);

        nextProductBeta.tenant(null);
        assertThat(nextProductBeta.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductBeta nextProductBeta = getNextProductBetaRandomSampleGenerator();
        NextOrderBeta nextOrderBetaBack = getNextOrderBetaRandomSampleGenerator();

        nextProductBeta.setOrder(nextOrderBetaBack);
        assertThat(nextProductBeta.getOrder()).isEqualTo(nextOrderBetaBack);

        nextProductBeta.order(null);
        assertThat(nextProductBeta.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductBeta nextProductBeta = getNextProductBetaRandomSampleGenerator();
        NextSupplierBeta nextSupplierBetaBack = getNextSupplierBetaRandomSampleGenerator();

        nextProductBeta.addSuppliers(nextSupplierBetaBack);
        assertThat(nextProductBeta.getSuppliers()).containsOnly(nextSupplierBetaBack);
        assertThat(nextSupplierBetaBack.getProducts()).containsOnly(nextProductBeta);

        nextProductBeta.removeSuppliers(nextSupplierBetaBack);
        assertThat(nextProductBeta.getSuppliers()).doesNotContain(nextSupplierBetaBack);
        assertThat(nextSupplierBetaBack.getProducts()).doesNotContain(nextProductBeta);

        nextProductBeta.suppliers(new HashSet<>(Set.of(nextSupplierBetaBack)));
        assertThat(nextProductBeta.getSuppliers()).containsOnly(nextSupplierBetaBack);
        assertThat(nextSupplierBetaBack.getProducts()).containsOnly(nextProductBeta);

        nextProductBeta.setSuppliers(new HashSet<>());
        assertThat(nextProductBeta.getSuppliers()).doesNotContain(nextSupplierBetaBack);
        assertThat(nextSupplierBetaBack.getProducts()).doesNotContain(nextProductBeta);
    }
}
