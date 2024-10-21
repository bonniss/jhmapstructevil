package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.ProductViTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierVi.class);
        SupplierVi supplierVi1 = getSupplierViSample1();
        SupplierVi supplierVi2 = new SupplierVi();
        assertThat(supplierVi1).isNotEqualTo(supplierVi2);

        supplierVi2.setId(supplierVi1.getId());
        assertThat(supplierVi1).isEqualTo(supplierVi2);

        supplierVi2 = getSupplierViSample2();
        assertThat(supplierVi1).isNotEqualTo(supplierVi2);
    }

    @Test
    void productsTest() {
        SupplierVi supplierVi = getSupplierViRandomSampleGenerator();
        ProductVi productViBack = getProductViRandomSampleGenerator();

        supplierVi.addProducts(productViBack);
        assertThat(supplierVi.getProducts()).containsOnly(productViBack);

        supplierVi.removeProducts(productViBack);
        assertThat(supplierVi.getProducts()).doesNotContain(productViBack);

        supplierVi.products(new HashSet<>(Set.of(productViBack)));
        assertThat(supplierVi.getProducts()).containsOnly(productViBack);

        supplierVi.setProducts(new HashSet<>());
        assertThat(supplierVi.getProducts()).doesNotContain(productViBack);
    }
}
