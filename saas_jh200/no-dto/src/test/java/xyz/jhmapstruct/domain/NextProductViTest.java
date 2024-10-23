package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryViTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderViTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductViTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductVi.class);
        NextProductVi nextProductVi1 = getNextProductViSample1();
        NextProductVi nextProductVi2 = new NextProductVi();
        assertThat(nextProductVi1).isNotEqualTo(nextProductVi2);

        nextProductVi2.setId(nextProductVi1.getId());
        assertThat(nextProductVi1).isEqualTo(nextProductVi2);

        nextProductVi2 = getNextProductViSample2();
        assertThat(nextProductVi1).isNotEqualTo(nextProductVi2);
    }

    @Test
    void categoryTest() {
        NextProductVi nextProductVi = getNextProductViRandomSampleGenerator();
        NextCategoryVi nextCategoryViBack = getNextCategoryViRandomSampleGenerator();

        nextProductVi.setCategory(nextCategoryViBack);
        assertThat(nextProductVi.getCategory()).isEqualTo(nextCategoryViBack);

        nextProductVi.category(null);
        assertThat(nextProductVi.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductVi nextProductVi = getNextProductViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductVi.setTenant(masterTenantBack);
        assertThat(nextProductVi.getTenant()).isEqualTo(masterTenantBack);

        nextProductVi.tenant(null);
        assertThat(nextProductVi.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductVi nextProductVi = getNextProductViRandomSampleGenerator();
        NextOrderVi nextOrderViBack = getNextOrderViRandomSampleGenerator();

        nextProductVi.setOrder(nextOrderViBack);
        assertThat(nextProductVi.getOrder()).isEqualTo(nextOrderViBack);

        nextProductVi.order(null);
        assertThat(nextProductVi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductVi nextProductVi = getNextProductViRandomSampleGenerator();
        NextSupplierVi nextSupplierViBack = getNextSupplierViRandomSampleGenerator();

        nextProductVi.addSuppliers(nextSupplierViBack);
        assertThat(nextProductVi.getSuppliers()).containsOnly(nextSupplierViBack);
        assertThat(nextSupplierViBack.getProducts()).containsOnly(nextProductVi);

        nextProductVi.removeSuppliers(nextSupplierViBack);
        assertThat(nextProductVi.getSuppliers()).doesNotContain(nextSupplierViBack);
        assertThat(nextSupplierViBack.getProducts()).doesNotContain(nextProductVi);

        nextProductVi.suppliers(new HashSet<>(Set.of(nextSupplierViBack)));
        assertThat(nextProductVi.getSuppliers()).containsOnly(nextSupplierViBack);
        assertThat(nextSupplierViBack.getProducts()).containsOnly(nextProductVi);

        nextProductVi.setSuppliers(new HashSet<>());
        assertThat(nextProductVi.getSuppliers()).doesNotContain(nextSupplierViBack);
        assertThat(nextSupplierViBack.getProducts()).doesNotContain(nextProductVi);
    }
}
