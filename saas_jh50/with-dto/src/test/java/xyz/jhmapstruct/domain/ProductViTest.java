package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderViTestSamples.*;
import static xyz.jhmapstruct.domain.ProductViTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductVi.class);
        ProductVi productVi1 = getProductViSample1();
        ProductVi productVi2 = new ProductVi();
        assertThat(productVi1).isNotEqualTo(productVi2);

        productVi2.setId(productVi1.getId());
        assertThat(productVi1).isEqualTo(productVi2);

        productVi2 = getProductViSample2();
        assertThat(productVi1).isNotEqualTo(productVi2);
    }

    @Test
    void categoryTest() {
        ProductVi productVi = getProductViRandomSampleGenerator();
        CategoryVi categoryViBack = getCategoryViRandomSampleGenerator();

        productVi.setCategory(categoryViBack);
        assertThat(productVi.getCategory()).isEqualTo(categoryViBack);

        productVi.category(null);
        assertThat(productVi.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        ProductVi productVi = getProductViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        productVi.setTenant(masterTenantBack);
        assertThat(productVi.getTenant()).isEqualTo(masterTenantBack);

        productVi.tenant(null);
        assertThat(productVi.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        ProductVi productVi = getProductViRandomSampleGenerator();
        OrderVi orderViBack = getOrderViRandomSampleGenerator();

        productVi.setOrder(orderViBack);
        assertThat(productVi.getOrder()).isEqualTo(orderViBack);

        productVi.order(null);
        assertThat(productVi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductVi productVi = getProductViRandomSampleGenerator();
        SupplierVi supplierViBack = getSupplierViRandomSampleGenerator();

        productVi.addSuppliers(supplierViBack);
        assertThat(productVi.getSuppliers()).containsOnly(supplierViBack);
        assertThat(supplierViBack.getProducts()).containsOnly(productVi);

        productVi.removeSuppliers(supplierViBack);
        assertThat(productVi.getSuppliers()).doesNotContain(supplierViBack);
        assertThat(supplierViBack.getProducts()).doesNotContain(productVi);

        productVi.suppliers(new HashSet<>(Set.of(supplierViBack)));
        assertThat(productVi.getSuppliers()).containsOnly(supplierViBack);
        assertThat(supplierViBack.getProducts()).containsOnly(productVi);

        productVi.setSuppliers(new HashSet<>());
        assertThat(productVi.getSuppliers()).doesNotContain(supplierViBack);
        assertThat(supplierViBack.getProducts()).doesNotContain(productVi);
    }
}
