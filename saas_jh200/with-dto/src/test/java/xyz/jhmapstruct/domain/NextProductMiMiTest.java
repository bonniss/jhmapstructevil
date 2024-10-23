package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierMiMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductMiMi.class);
        NextProductMiMi nextProductMiMi1 = getNextProductMiMiSample1();
        NextProductMiMi nextProductMiMi2 = new NextProductMiMi();
        assertThat(nextProductMiMi1).isNotEqualTo(nextProductMiMi2);

        nextProductMiMi2.setId(nextProductMiMi1.getId());
        assertThat(nextProductMiMi1).isEqualTo(nextProductMiMi2);

        nextProductMiMi2 = getNextProductMiMiSample2();
        assertThat(nextProductMiMi1).isNotEqualTo(nextProductMiMi2);
    }

    @Test
    void categoryTest() {
        NextProductMiMi nextProductMiMi = getNextProductMiMiRandomSampleGenerator();
        NextCategoryMiMi nextCategoryMiMiBack = getNextCategoryMiMiRandomSampleGenerator();

        nextProductMiMi.setCategory(nextCategoryMiMiBack);
        assertThat(nextProductMiMi.getCategory()).isEqualTo(nextCategoryMiMiBack);

        nextProductMiMi.category(null);
        assertThat(nextProductMiMi.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductMiMi nextProductMiMi = getNextProductMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductMiMi.setTenant(masterTenantBack);
        assertThat(nextProductMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextProductMiMi.tenant(null);
        assertThat(nextProductMiMi.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductMiMi nextProductMiMi = getNextProductMiMiRandomSampleGenerator();
        NextOrderMiMi nextOrderMiMiBack = getNextOrderMiMiRandomSampleGenerator();

        nextProductMiMi.setOrder(nextOrderMiMiBack);
        assertThat(nextProductMiMi.getOrder()).isEqualTo(nextOrderMiMiBack);

        nextProductMiMi.order(null);
        assertThat(nextProductMiMi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductMiMi nextProductMiMi = getNextProductMiMiRandomSampleGenerator();
        NextSupplierMiMi nextSupplierMiMiBack = getNextSupplierMiMiRandomSampleGenerator();

        nextProductMiMi.addSuppliers(nextSupplierMiMiBack);
        assertThat(nextProductMiMi.getSuppliers()).containsOnly(nextSupplierMiMiBack);
        assertThat(nextSupplierMiMiBack.getProducts()).containsOnly(nextProductMiMi);

        nextProductMiMi.removeSuppliers(nextSupplierMiMiBack);
        assertThat(nextProductMiMi.getSuppliers()).doesNotContain(nextSupplierMiMiBack);
        assertThat(nextSupplierMiMiBack.getProducts()).doesNotContain(nextProductMiMi);

        nextProductMiMi.suppliers(new HashSet<>(Set.of(nextSupplierMiMiBack)));
        assertThat(nextProductMiMi.getSuppliers()).containsOnly(nextSupplierMiMiBack);
        assertThat(nextSupplierMiMiBack.getProducts()).containsOnly(nextProductMiMi);

        nextProductMiMi.setSuppliers(new HashSet<>());
        assertThat(nextProductMiMi.getSuppliers()).doesNotContain(nextSupplierMiMiBack);
        assertThat(nextSupplierMiMiBack.getProducts()).doesNotContain(nextProductMiMi);
    }
}
