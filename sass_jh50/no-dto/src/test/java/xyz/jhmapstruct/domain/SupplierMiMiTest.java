package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierMiMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierMiMi.class);
        SupplierMiMi supplierMiMi1 = getSupplierMiMiSample1();
        SupplierMiMi supplierMiMi2 = new SupplierMiMi();
        assertThat(supplierMiMi1).isNotEqualTo(supplierMiMi2);

        supplierMiMi2.setId(supplierMiMi1.getId());
        assertThat(supplierMiMi1).isEqualTo(supplierMiMi2);

        supplierMiMi2 = getSupplierMiMiSample2();
        assertThat(supplierMiMi1).isNotEqualTo(supplierMiMi2);
    }

    @Test
    void tenantTest() {
        SupplierMiMi supplierMiMi = getSupplierMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        supplierMiMi.setTenant(masterTenantBack);
        assertThat(supplierMiMi.getTenant()).isEqualTo(masterTenantBack);

        supplierMiMi.tenant(null);
        assertThat(supplierMiMi.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        SupplierMiMi supplierMiMi = getSupplierMiMiRandomSampleGenerator();
        ProductMiMi productMiMiBack = getProductMiMiRandomSampleGenerator();

        supplierMiMi.addProducts(productMiMiBack);
        assertThat(supplierMiMi.getProducts()).containsOnly(productMiMiBack);

        supplierMiMi.removeProducts(productMiMiBack);
        assertThat(supplierMiMi.getProducts()).doesNotContain(productMiMiBack);

        supplierMiMi.products(new HashSet<>(Set.of(productMiMiBack)));
        assertThat(supplierMiMi.getProducts()).containsOnly(productMiMiBack);

        supplierMiMi.setProducts(new HashSet<>());
        assertThat(supplierMiMi.getProducts()).doesNotContain(productMiMiBack);
    }
}
