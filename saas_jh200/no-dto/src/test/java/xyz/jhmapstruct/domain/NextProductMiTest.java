package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryMiTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductMiTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductMi.class);
        NextProductMi nextProductMi1 = getNextProductMiSample1();
        NextProductMi nextProductMi2 = new NextProductMi();
        assertThat(nextProductMi1).isNotEqualTo(nextProductMi2);

        nextProductMi2.setId(nextProductMi1.getId());
        assertThat(nextProductMi1).isEqualTo(nextProductMi2);

        nextProductMi2 = getNextProductMiSample2();
        assertThat(nextProductMi1).isNotEqualTo(nextProductMi2);
    }

    @Test
    void categoryTest() {
        NextProductMi nextProductMi = getNextProductMiRandomSampleGenerator();
        CategoryMi categoryMiBack = getCategoryMiRandomSampleGenerator();

        nextProductMi.setCategory(categoryMiBack);
        assertThat(nextProductMi.getCategory()).isEqualTo(categoryMiBack);

        nextProductMi.category(null);
        assertThat(nextProductMi.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductMi nextProductMi = getNextProductMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductMi.setTenant(masterTenantBack);
        assertThat(nextProductMi.getTenant()).isEqualTo(masterTenantBack);

        nextProductMi.tenant(null);
        assertThat(nextProductMi.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductMi nextProductMi = getNextProductMiRandomSampleGenerator();
        NextOrderMi nextOrderMiBack = getNextOrderMiRandomSampleGenerator();

        nextProductMi.setOrder(nextOrderMiBack);
        assertThat(nextProductMi.getOrder()).isEqualTo(nextOrderMiBack);

        nextProductMi.order(null);
        assertThat(nextProductMi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductMi nextProductMi = getNextProductMiRandomSampleGenerator();
        SupplierMi supplierMiBack = getSupplierMiRandomSampleGenerator();

        nextProductMi.addSuppliers(supplierMiBack);
        assertThat(nextProductMi.getSuppliers()).containsOnly(supplierMiBack);
        assertThat(supplierMiBack.getProducts()).containsOnly(nextProductMi);

        nextProductMi.removeSuppliers(supplierMiBack);
        assertThat(nextProductMi.getSuppliers()).doesNotContain(supplierMiBack);
        assertThat(supplierMiBack.getProducts()).doesNotContain(nextProductMi);

        nextProductMi.suppliers(new HashSet<>(Set.of(supplierMiBack)));
        assertThat(nextProductMi.getSuppliers()).containsOnly(supplierMiBack);
        assertThat(supplierMiBack.getProducts()).containsOnly(nextProductMi);

        nextProductMi.setSuppliers(new HashSet<>());
        assertThat(nextProductMi.getSuppliers()).doesNotContain(supplierMiBack);
        assertThat(supplierMiBack.getProducts()).doesNotContain(nextProductMi);
    }
}
