package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.ProductViViTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierViViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierViVi.class);
        SupplierViVi supplierViVi1 = getSupplierViViSample1();
        SupplierViVi supplierViVi2 = new SupplierViVi();
        assertThat(supplierViVi1).isNotEqualTo(supplierViVi2);

        supplierViVi2.setId(supplierViVi1.getId());
        assertThat(supplierViVi1).isEqualTo(supplierViVi2);

        supplierViVi2 = getSupplierViViSample2();
        assertThat(supplierViVi1).isNotEqualTo(supplierViVi2);
    }

    @Test
    void productsTest() {
        SupplierViVi supplierViVi = getSupplierViViRandomSampleGenerator();
        ProductViVi productViViBack = getProductViViRandomSampleGenerator();

        supplierViVi.addProducts(productViViBack);
        assertThat(supplierViVi.getProducts()).containsOnly(productViViBack);

        supplierViVi.removeProducts(productViViBack);
        assertThat(supplierViVi.getProducts()).doesNotContain(productViViBack);

        supplierViVi.products(new HashSet<>(Set.of(productViViBack)));
        assertThat(supplierViVi.getProducts()).containsOnly(productViViBack);

        supplierViVi.setProducts(new HashSet<>());
        assertThat(supplierViVi.getProducts()).doesNotContain(productViViBack);
    }
}
