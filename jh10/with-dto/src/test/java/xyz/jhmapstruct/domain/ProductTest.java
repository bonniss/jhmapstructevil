package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryTestSamples.*;
import static xyz.jhmapstruct.domain.OrderTestSamples.*;
import static xyz.jhmapstruct.domain.ProductTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void categoryTest() {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }

    @Test
    void orderTest() {
        Product product = getProductRandomSampleGenerator();
        Order orderBack = getOrderRandomSampleGenerator();

        product.setOrder(orderBack);
        assertThat(product.getOrder()).isEqualTo(orderBack);

        product.order(null);
        assertThat(product.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        Product product = getProductRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        product.addSuppliers(supplierBack);
        assertThat(product.getSuppliers()).containsOnly(supplierBack);
        assertThat(supplierBack.getProducts()).containsOnly(product);

        product.removeSuppliers(supplierBack);
        assertThat(product.getSuppliers()).doesNotContain(supplierBack);
        assertThat(supplierBack.getProducts()).doesNotContain(product);

        product.suppliers(new HashSet<>(Set.of(supplierBack)));
        assertThat(product.getSuppliers()).containsOnly(supplierBack);
        assertThat(supplierBack.getProducts()).containsOnly(product);

        product.setSuppliers(new HashSet<>());
        assertThat(product.getSuppliers()).doesNotContain(supplierBack);
        assertThat(supplierBack.getProducts()).doesNotContain(product);
    }
}
