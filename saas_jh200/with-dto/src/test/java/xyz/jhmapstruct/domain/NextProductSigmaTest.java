package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategorySigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierSigmaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductSigma.class);
        NextProductSigma nextProductSigma1 = getNextProductSigmaSample1();
        NextProductSigma nextProductSigma2 = new NextProductSigma();
        assertThat(nextProductSigma1).isNotEqualTo(nextProductSigma2);

        nextProductSigma2.setId(nextProductSigma1.getId());
        assertThat(nextProductSigma1).isEqualTo(nextProductSigma2);

        nextProductSigma2 = getNextProductSigmaSample2();
        assertThat(nextProductSigma1).isNotEqualTo(nextProductSigma2);
    }

    @Test
    void categoryTest() {
        NextProductSigma nextProductSigma = getNextProductSigmaRandomSampleGenerator();
        NextCategorySigma nextCategorySigmaBack = getNextCategorySigmaRandomSampleGenerator();

        nextProductSigma.setCategory(nextCategorySigmaBack);
        assertThat(nextProductSigma.getCategory()).isEqualTo(nextCategorySigmaBack);

        nextProductSigma.category(null);
        assertThat(nextProductSigma.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductSigma nextProductSigma = getNextProductSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductSigma.setTenant(masterTenantBack);
        assertThat(nextProductSigma.getTenant()).isEqualTo(masterTenantBack);

        nextProductSigma.tenant(null);
        assertThat(nextProductSigma.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductSigma nextProductSigma = getNextProductSigmaRandomSampleGenerator();
        NextOrderSigma nextOrderSigmaBack = getNextOrderSigmaRandomSampleGenerator();

        nextProductSigma.setOrder(nextOrderSigmaBack);
        assertThat(nextProductSigma.getOrder()).isEqualTo(nextOrderSigmaBack);

        nextProductSigma.order(null);
        assertThat(nextProductSigma.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductSigma nextProductSigma = getNextProductSigmaRandomSampleGenerator();
        NextSupplierSigma nextSupplierSigmaBack = getNextSupplierSigmaRandomSampleGenerator();

        nextProductSigma.addSuppliers(nextSupplierSigmaBack);
        assertThat(nextProductSigma.getSuppliers()).containsOnly(nextSupplierSigmaBack);
        assertThat(nextSupplierSigmaBack.getProducts()).containsOnly(nextProductSigma);

        nextProductSigma.removeSuppliers(nextSupplierSigmaBack);
        assertThat(nextProductSigma.getSuppliers()).doesNotContain(nextSupplierSigmaBack);
        assertThat(nextSupplierSigmaBack.getProducts()).doesNotContain(nextProductSigma);

        nextProductSigma.suppliers(new HashSet<>(Set.of(nextSupplierSigmaBack)));
        assertThat(nextProductSigma.getSuppliers()).containsOnly(nextSupplierSigmaBack);
        assertThat(nextSupplierSigmaBack.getProducts()).containsOnly(nextProductSigma);

        nextProductSigma.setSuppliers(new HashSet<>());
        assertThat(nextProductSigma.getSuppliers()).doesNotContain(nextSupplierSigmaBack);
        assertThat(nextSupplierSigmaBack.getProducts()).doesNotContain(nextProductSigma);
    }
}
