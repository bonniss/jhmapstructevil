package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProduct.class);
        NextProduct nextProduct1 = getNextProductSample1();
        NextProduct nextProduct2 = new NextProduct();
        assertThat(nextProduct1).isNotEqualTo(nextProduct2);

        nextProduct2.setId(nextProduct1.getId());
        assertThat(nextProduct1).isEqualTo(nextProduct2);

        nextProduct2 = getNextProductSample2();
        assertThat(nextProduct1).isNotEqualTo(nextProduct2);
    }

    @Test
    void categoryTest() {
        NextProduct nextProduct = getNextProductRandomSampleGenerator();
        NextCategory nextCategoryBack = getNextCategoryRandomSampleGenerator();

        nextProduct.setCategory(nextCategoryBack);
        assertThat(nextProduct.getCategory()).isEqualTo(nextCategoryBack);

        nextProduct.category(null);
        assertThat(nextProduct.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProduct nextProduct = getNextProductRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProduct.setTenant(masterTenantBack);
        assertThat(nextProduct.getTenant()).isEqualTo(masterTenantBack);

        nextProduct.tenant(null);
        assertThat(nextProduct.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProduct nextProduct = getNextProductRandomSampleGenerator();
        NextOrder nextOrderBack = getNextOrderRandomSampleGenerator();

        nextProduct.setOrder(nextOrderBack);
        assertThat(nextProduct.getOrder()).isEqualTo(nextOrderBack);

        nextProduct.order(null);
        assertThat(nextProduct.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProduct nextProduct = getNextProductRandomSampleGenerator();
        NextSupplier nextSupplierBack = getNextSupplierRandomSampleGenerator();

        nextProduct.addSuppliers(nextSupplierBack);
        assertThat(nextProduct.getSuppliers()).containsOnly(nextSupplierBack);
        assertThat(nextSupplierBack.getProducts()).containsOnly(nextProduct);

        nextProduct.removeSuppliers(nextSupplierBack);
        assertThat(nextProduct.getSuppliers()).doesNotContain(nextSupplierBack);
        assertThat(nextSupplierBack.getProducts()).doesNotContain(nextProduct);

        nextProduct.suppliers(new HashSet<>(Set.of(nextSupplierBack)));
        assertThat(nextProduct.getSuppliers()).containsOnly(nextSupplierBack);
        assertThat(nextSupplierBack.getProducts()).containsOnly(nextProduct);

        nextProduct.setSuppliers(new HashSet<>());
        assertThat(nextProduct.getSuppliers()).doesNotContain(nextSupplierBack);
        assertThat(nextSupplierBack.getProducts()).doesNotContain(nextProduct);
    }
}
