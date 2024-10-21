package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryViViTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderViViTestSamples.*;
import static xyz.jhmapstruct.domain.ProductViViTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierViViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductViVi.class);
        ProductViVi productViVi1 = getProductViViSample1();
        ProductViVi productViVi2 = new ProductViVi();
        assertThat(productViVi1).isNotEqualTo(productViVi2);

        productViVi2.setId(productViVi1.getId());
        assertThat(productViVi1).isEqualTo(productViVi2);

        productViVi2 = getProductViViSample2();
        assertThat(productViVi1).isNotEqualTo(productViVi2);
    }

    @Test
    void categoryTest() {
        ProductViVi productViVi = getProductViViRandomSampleGenerator();
        CategoryViVi categoryViViBack = getCategoryViViRandomSampleGenerator();

        productViVi.setCategory(categoryViViBack);
        assertThat(productViVi.getCategory()).isEqualTo(categoryViViBack);

        productViVi.category(null);
        assertThat(productViVi.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        ProductViVi productViVi = getProductViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        productViVi.setTenant(masterTenantBack);
        assertThat(productViVi.getTenant()).isEqualTo(masterTenantBack);

        productViVi.tenant(null);
        assertThat(productViVi.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        ProductViVi productViVi = getProductViViRandomSampleGenerator();
        OrderViVi orderViViBack = getOrderViViRandomSampleGenerator();

        productViVi.setOrder(orderViViBack);
        assertThat(productViVi.getOrder()).isEqualTo(orderViViBack);

        productViVi.order(null);
        assertThat(productViVi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductViVi productViVi = getProductViViRandomSampleGenerator();
        SupplierViVi supplierViViBack = getSupplierViViRandomSampleGenerator();

        productViVi.addSuppliers(supplierViViBack);
        assertThat(productViVi.getSuppliers()).containsOnly(supplierViViBack);
        assertThat(supplierViViBack.getProducts()).containsOnly(productViVi);

        productViVi.removeSuppliers(supplierViViBack);
        assertThat(productViVi.getSuppliers()).doesNotContain(supplierViViBack);
        assertThat(supplierViViBack.getProducts()).doesNotContain(productViVi);

        productViVi.suppliers(new HashSet<>(Set.of(supplierViViBack)));
        assertThat(productViVi.getSuppliers()).containsOnly(supplierViViBack);
        assertThat(supplierViViBack.getProducts()).containsOnly(productViVi);

        productViVi.setSuppliers(new HashSet<>());
        assertThat(productViVi.getSuppliers()).doesNotContain(supplierViViBack);
        assertThat(supplierViViBack.getProducts()).doesNotContain(productViVi);
    }
}
