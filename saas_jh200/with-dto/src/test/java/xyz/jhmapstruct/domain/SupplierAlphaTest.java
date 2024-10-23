package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierAlphaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierAlpha.class);
        SupplierAlpha supplierAlpha1 = getSupplierAlphaSample1();
        SupplierAlpha supplierAlpha2 = new SupplierAlpha();
        assertThat(supplierAlpha1).isNotEqualTo(supplierAlpha2);

        supplierAlpha2.setId(supplierAlpha1.getId());
        assertThat(supplierAlpha1).isEqualTo(supplierAlpha2);

        supplierAlpha2 = getSupplierAlphaSample2();
        assertThat(supplierAlpha1).isNotEqualTo(supplierAlpha2);
    }

    @Test
    void tenantTest() {
        SupplierAlpha supplierAlpha = getSupplierAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        supplierAlpha.setTenant(masterTenantBack);
        assertThat(supplierAlpha.getTenant()).isEqualTo(masterTenantBack);

        supplierAlpha.tenant(null);
        assertThat(supplierAlpha.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        SupplierAlpha supplierAlpha = getSupplierAlphaRandomSampleGenerator();
        ProductAlpha productAlphaBack = getProductAlphaRandomSampleGenerator();

        supplierAlpha.addProducts(productAlphaBack);
        assertThat(supplierAlpha.getProducts()).containsOnly(productAlphaBack);

        supplierAlpha.removeProducts(productAlphaBack);
        assertThat(supplierAlpha.getProducts()).doesNotContain(productAlphaBack);

        supplierAlpha.products(new HashSet<>(Set.of(productAlphaBack)));
        assertThat(supplierAlpha.getProducts()).containsOnly(productAlphaBack);

        supplierAlpha.setProducts(new HashSet<>());
        assertThat(supplierAlpha.getProducts()).doesNotContain(productAlphaBack);
    }
}
