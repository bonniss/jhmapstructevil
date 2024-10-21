package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.ProductTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplier.class);
        Supplier supplier1 = getSupplierSample1();
        Supplier supplier2 = new Supplier();
        assertThat(supplier1).isNotEqualTo(supplier2);

        supplier2.setId(supplier1.getId());
        assertThat(supplier1).isEqualTo(supplier2);

        supplier2 = getSupplierSample2();
        assertThat(supplier1).isNotEqualTo(supplier2);
    }

    @Test
    void productsTest() {
        Supplier supplier = getSupplierRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        supplier.addProducts(productBack);
        assertThat(supplier.getProducts()).containsOnly(productBack);

        supplier.removeProducts(productBack);
        assertThat(supplier.getProducts()).doesNotContain(productBack);

        supplier.products(new HashSet<>(Set.of(productBack)));
        assertThat(supplier.getProducts()).containsOnly(productBack);

        supplier.setProducts(new HashSet<>());
        assertThat(supplier.getProducts()).doesNotContain(productBack);
    }
}
