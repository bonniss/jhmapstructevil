package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierViViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductViVi.class);
        NextProductViVi nextProductViVi1 = getNextProductViViSample1();
        NextProductViVi nextProductViVi2 = new NextProductViVi();
        assertThat(nextProductViVi1).isNotEqualTo(nextProductViVi2);

        nextProductViVi2.setId(nextProductViVi1.getId());
        assertThat(nextProductViVi1).isEqualTo(nextProductViVi2);

        nextProductViVi2 = getNextProductViViSample2();
        assertThat(nextProductViVi1).isNotEqualTo(nextProductViVi2);
    }

    @Test
    void categoryTest() {
        NextProductViVi nextProductViVi = getNextProductViViRandomSampleGenerator();
        NextCategoryViVi nextCategoryViViBack = getNextCategoryViViRandomSampleGenerator();

        nextProductViVi.setCategory(nextCategoryViViBack);
        assertThat(nextProductViVi.getCategory()).isEqualTo(nextCategoryViViBack);

        nextProductViVi.category(null);
        assertThat(nextProductViVi.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductViVi nextProductViVi = getNextProductViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductViVi.setTenant(masterTenantBack);
        assertThat(nextProductViVi.getTenant()).isEqualTo(masterTenantBack);

        nextProductViVi.tenant(null);
        assertThat(nextProductViVi.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductViVi nextProductViVi = getNextProductViViRandomSampleGenerator();
        NextOrderViVi nextOrderViViBack = getNextOrderViViRandomSampleGenerator();

        nextProductViVi.setOrder(nextOrderViViBack);
        assertThat(nextProductViVi.getOrder()).isEqualTo(nextOrderViViBack);

        nextProductViVi.order(null);
        assertThat(nextProductViVi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductViVi nextProductViVi = getNextProductViViRandomSampleGenerator();
        NextSupplierViVi nextSupplierViViBack = getNextSupplierViViRandomSampleGenerator();

        nextProductViVi.addSuppliers(nextSupplierViViBack);
        assertThat(nextProductViVi.getSuppliers()).containsOnly(nextSupplierViViBack);
        assertThat(nextSupplierViViBack.getProducts()).containsOnly(nextProductViVi);

        nextProductViVi.removeSuppliers(nextSupplierViViBack);
        assertThat(nextProductViVi.getSuppliers()).doesNotContain(nextSupplierViViBack);
        assertThat(nextSupplierViViBack.getProducts()).doesNotContain(nextProductViVi);

        nextProductViVi.suppliers(new HashSet<>(Set.of(nextSupplierViViBack)));
        assertThat(nextProductViVi.getSuppliers()).containsOnly(nextSupplierViViBack);
        assertThat(nextSupplierViViBack.getProducts()).containsOnly(nextProductViVi);

        nextProductViVi.setSuppliers(new HashSet<>());
        assertThat(nextProductViVi.getSuppliers()).doesNotContain(nextSupplierViViBack);
        assertThat(nextSupplierViViBack.getProducts()).doesNotContain(nextProductViVi);
    }
}
