package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.OrderMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierMiMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductMiMi.class);
        ProductMiMi productMiMi1 = getProductMiMiSample1();
        ProductMiMi productMiMi2 = new ProductMiMi();
        assertThat(productMiMi1).isNotEqualTo(productMiMi2);

        productMiMi2.setId(productMiMi1.getId());
        assertThat(productMiMi1).isEqualTo(productMiMi2);

        productMiMi2 = getProductMiMiSample2();
        assertThat(productMiMi1).isNotEqualTo(productMiMi2);
    }

    @Test
    void categoryTest() {
        ProductMiMi productMiMi = getProductMiMiRandomSampleGenerator();
        CategoryMiMi categoryMiMiBack = getCategoryMiMiRandomSampleGenerator();

        productMiMi.setCategory(categoryMiMiBack);
        assertThat(productMiMi.getCategory()).isEqualTo(categoryMiMiBack);

        productMiMi.category(null);
        assertThat(productMiMi.getCategory()).isNull();
    }

    @Test
    void orderTest() {
        ProductMiMi productMiMi = getProductMiMiRandomSampleGenerator();
        OrderMiMi orderMiMiBack = getOrderMiMiRandomSampleGenerator();

        productMiMi.setOrder(orderMiMiBack);
        assertThat(productMiMi.getOrder()).isEqualTo(orderMiMiBack);

        productMiMi.order(null);
        assertThat(productMiMi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductMiMi productMiMi = getProductMiMiRandomSampleGenerator();
        SupplierMiMi supplierMiMiBack = getSupplierMiMiRandomSampleGenerator();

        productMiMi.addSuppliers(supplierMiMiBack);
        assertThat(productMiMi.getSuppliers()).containsOnly(supplierMiMiBack);
        assertThat(supplierMiMiBack.getProducts()).containsOnly(productMiMi);

        productMiMi.removeSuppliers(supplierMiMiBack);
        assertThat(productMiMi.getSuppliers()).doesNotContain(supplierMiMiBack);
        assertThat(supplierMiMiBack.getProducts()).doesNotContain(productMiMi);

        productMiMi.suppliers(new HashSet<>(Set.of(supplierMiMiBack)));
        assertThat(productMiMi.getSuppliers()).containsOnly(supplierMiMiBack);
        assertThat(supplierMiMiBack.getProducts()).containsOnly(productMiMi);

        productMiMi.setSuppliers(new HashSet<>());
        assertThat(productMiMi.getSuppliers()).doesNotContain(supplierMiMiBack);
        assertThat(supplierMiMiBack.getProducts()).doesNotContain(productMiMi);
    }
}
