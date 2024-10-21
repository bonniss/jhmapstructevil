package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.ProductMiTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierMi.class);
        SupplierMi supplierMi1 = getSupplierMiSample1();
        SupplierMi supplierMi2 = new SupplierMi();
        assertThat(supplierMi1).isNotEqualTo(supplierMi2);

        supplierMi2.setId(supplierMi1.getId());
        assertThat(supplierMi1).isEqualTo(supplierMi2);

        supplierMi2 = getSupplierMiSample2();
        assertThat(supplierMi1).isNotEqualTo(supplierMi2);
    }

    @Test
    void productsTest() {
        SupplierMi supplierMi = getSupplierMiRandomSampleGenerator();
        ProductMi productMiBack = getProductMiRandomSampleGenerator();

        supplierMi.addProducts(productMiBack);
        assertThat(supplierMi.getProducts()).containsOnly(productMiBack);

        supplierMi.removeProducts(productMiBack);
        assertThat(supplierMi.getProducts()).doesNotContain(productMiBack);

        supplierMi.products(new HashSet<>(Set.of(productMiBack)));
        assertThat(supplierMi.getProducts()).containsOnly(productMiBack);

        supplierMi.setProducts(new HashSet<>());
        assertThat(supplierMi.getProducts()).doesNotContain(productMiBack);
    }
}
