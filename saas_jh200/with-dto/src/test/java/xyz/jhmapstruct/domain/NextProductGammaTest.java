package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierGammaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductGamma.class);
        NextProductGamma nextProductGamma1 = getNextProductGammaSample1();
        NextProductGamma nextProductGamma2 = new NextProductGamma();
        assertThat(nextProductGamma1).isNotEqualTo(nextProductGamma2);

        nextProductGamma2.setId(nextProductGamma1.getId());
        assertThat(nextProductGamma1).isEqualTo(nextProductGamma2);

        nextProductGamma2 = getNextProductGammaSample2();
        assertThat(nextProductGamma1).isNotEqualTo(nextProductGamma2);
    }

    @Test
    void categoryTest() {
        NextProductGamma nextProductGamma = getNextProductGammaRandomSampleGenerator();
        NextCategoryGamma nextCategoryGammaBack = getNextCategoryGammaRandomSampleGenerator();

        nextProductGamma.setCategory(nextCategoryGammaBack);
        assertThat(nextProductGamma.getCategory()).isEqualTo(nextCategoryGammaBack);

        nextProductGamma.category(null);
        assertThat(nextProductGamma.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductGamma nextProductGamma = getNextProductGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductGamma.setTenant(masterTenantBack);
        assertThat(nextProductGamma.getTenant()).isEqualTo(masterTenantBack);

        nextProductGamma.tenant(null);
        assertThat(nextProductGamma.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductGamma nextProductGamma = getNextProductGammaRandomSampleGenerator();
        NextOrderGamma nextOrderGammaBack = getNextOrderGammaRandomSampleGenerator();

        nextProductGamma.setOrder(nextOrderGammaBack);
        assertThat(nextProductGamma.getOrder()).isEqualTo(nextOrderGammaBack);

        nextProductGamma.order(null);
        assertThat(nextProductGamma.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductGamma nextProductGamma = getNextProductGammaRandomSampleGenerator();
        NextSupplierGamma nextSupplierGammaBack = getNextSupplierGammaRandomSampleGenerator();

        nextProductGamma.addSuppliers(nextSupplierGammaBack);
        assertThat(nextProductGamma.getSuppliers()).containsOnly(nextSupplierGammaBack);
        assertThat(nextSupplierGammaBack.getProducts()).containsOnly(nextProductGamma);

        nextProductGamma.removeSuppliers(nextSupplierGammaBack);
        assertThat(nextProductGamma.getSuppliers()).doesNotContain(nextSupplierGammaBack);
        assertThat(nextSupplierGammaBack.getProducts()).doesNotContain(nextProductGamma);

        nextProductGamma.suppliers(new HashSet<>(Set.of(nextSupplierGammaBack)));
        assertThat(nextProductGamma.getSuppliers()).containsOnly(nextSupplierGammaBack);
        assertThat(nextSupplierGammaBack.getProducts()).containsOnly(nextProductGamma);

        nextProductGamma.setSuppliers(new HashSet<>());
        assertThat(nextProductGamma.getSuppliers()).doesNotContain(nextSupplierGammaBack);
        assertThat(nextSupplierGammaBack.getProducts()).doesNotContain(nextProductGamma);
    }
}
