package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryMiTestSamples.*;
import static xyz.jhmapstruct.domain.OrderMiTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductMi.class);
        ProductMi productMi1 = getProductMiSample1();
        ProductMi productMi2 = new ProductMi();
        assertThat(productMi1).isNotEqualTo(productMi2);

        productMi2.setId(productMi1.getId());
        assertThat(productMi1).isEqualTo(productMi2);

        productMi2 = getProductMiSample2();
        assertThat(productMi1).isNotEqualTo(productMi2);
    }

    @Test
    void categoryTest() {
        ProductMi productMi = getProductMiRandomSampleGenerator();
        CategoryMi categoryMiBack = getCategoryMiRandomSampleGenerator();

        productMi.setCategory(categoryMiBack);
        assertThat(productMi.getCategory()).isEqualTo(categoryMiBack);

        productMi.category(null);
        assertThat(productMi.getCategory()).isNull();
    }

    @Test
    void orderTest() {
        ProductMi productMi = getProductMiRandomSampleGenerator();
        OrderMi orderMiBack = getOrderMiRandomSampleGenerator();

        productMi.setOrder(orderMiBack);
        assertThat(productMi.getOrder()).isEqualTo(orderMiBack);

        productMi.order(null);
        assertThat(productMi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductMi productMi = getProductMiRandomSampleGenerator();
        SupplierMi supplierMiBack = getSupplierMiRandomSampleGenerator();

        productMi.addSuppliers(supplierMiBack);
        assertThat(productMi.getSuppliers()).containsOnly(supplierMiBack);
        assertThat(supplierMiBack.getProducts()).containsOnly(productMi);

        productMi.removeSuppliers(supplierMiBack);
        assertThat(productMi.getSuppliers()).doesNotContain(supplierMiBack);
        assertThat(supplierMiBack.getProducts()).doesNotContain(productMi);

        productMi.suppliers(new HashSet<>(Set.of(supplierMiBack)));
        assertThat(productMi.getSuppliers()).containsOnly(supplierMiBack);
        assertThat(supplierMiBack.getProducts()).containsOnly(productMi);

        productMi.setSuppliers(new HashSet<>());
        assertThat(productMi.getSuppliers()).doesNotContain(supplierMiBack);
        assertThat(supplierMiBack.getProducts()).doesNotContain(productMi);
    }
}
