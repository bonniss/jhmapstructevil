package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierAlphaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductAlpha.class);
        NextProductAlpha nextProductAlpha1 = getNextProductAlphaSample1();
        NextProductAlpha nextProductAlpha2 = new NextProductAlpha();
        assertThat(nextProductAlpha1).isNotEqualTo(nextProductAlpha2);

        nextProductAlpha2.setId(nextProductAlpha1.getId());
        assertThat(nextProductAlpha1).isEqualTo(nextProductAlpha2);

        nextProductAlpha2 = getNextProductAlphaSample2();
        assertThat(nextProductAlpha1).isNotEqualTo(nextProductAlpha2);
    }

    @Test
    void categoryTest() {
        NextProductAlpha nextProductAlpha = getNextProductAlphaRandomSampleGenerator();
        NextCategoryAlpha nextCategoryAlphaBack = getNextCategoryAlphaRandomSampleGenerator();

        nextProductAlpha.setCategory(nextCategoryAlphaBack);
        assertThat(nextProductAlpha.getCategory()).isEqualTo(nextCategoryAlphaBack);

        nextProductAlpha.category(null);
        assertThat(nextProductAlpha.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductAlpha nextProductAlpha = getNextProductAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductAlpha.setTenant(masterTenantBack);
        assertThat(nextProductAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextProductAlpha.tenant(null);
        assertThat(nextProductAlpha.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductAlpha nextProductAlpha = getNextProductAlphaRandomSampleGenerator();
        NextOrderAlpha nextOrderAlphaBack = getNextOrderAlphaRandomSampleGenerator();

        nextProductAlpha.setOrder(nextOrderAlphaBack);
        assertThat(nextProductAlpha.getOrder()).isEqualTo(nextOrderAlphaBack);

        nextProductAlpha.order(null);
        assertThat(nextProductAlpha.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductAlpha nextProductAlpha = getNextProductAlphaRandomSampleGenerator();
        NextSupplierAlpha nextSupplierAlphaBack = getNextSupplierAlphaRandomSampleGenerator();

        nextProductAlpha.addSuppliers(nextSupplierAlphaBack);
        assertThat(nextProductAlpha.getSuppliers()).containsOnly(nextSupplierAlphaBack);
        assertThat(nextSupplierAlphaBack.getProducts()).containsOnly(nextProductAlpha);

        nextProductAlpha.removeSuppliers(nextSupplierAlphaBack);
        assertThat(nextProductAlpha.getSuppliers()).doesNotContain(nextSupplierAlphaBack);
        assertThat(nextSupplierAlphaBack.getProducts()).doesNotContain(nextProductAlpha);

        nextProductAlpha.suppliers(new HashSet<>(Set.of(nextSupplierAlphaBack)));
        assertThat(nextProductAlpha.getSuppliers()).containsOnly(nextSupplierAlphaBack);
        assertThat(nextSupplierAlphaBack.getProducts()).containsOnly(nextProductAlpha);

        nextProductAlpha.setSuppliers(new HashSet<>());
        assertThat(nextProductAlpha.getSuppliers()).doesNotContain(nextSupplierAlphaBack);
        assertThat(nextSupplierAlphaBack.getProducts()).doesNotContain(nextProductAlpha);
    }
}
