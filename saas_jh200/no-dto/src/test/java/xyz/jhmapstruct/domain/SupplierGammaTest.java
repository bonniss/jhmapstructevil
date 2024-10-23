package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductGammaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierGammaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierGamma.class);
        SupplierGamma supplierGamma1 = getSupplierGammaSample1();
        SupplierGamma supplierGamma2 = new SupplierGamma();
        assertThat(supplierGamma1).isNotEqualTo(supplierGamma2);

        supplierGamma2.setId(supplierGamma1.getId());
        assertThat(supplierGamma1).isEqualTo(supplierGamma2);

        supplierGamma2 = getSupplierGammaSample2();
        assertThat(supplierGamma1).isNotEqualTo(supplierGamma2);
    }

    @Test
    void tenantTest() {
        SupplierGamma supplierGamma = getSupplierGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        supplierGamma.setTenant(masterTenantBack);
        assertThat(supplierGamma.getTenant()).isEqualTo(masterTenantBack);

        supplierGamma.tenant(null);
        assertThat(supplierGamma.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        SupplierGamma supplierGamma = getSupplierGammaRandomSampleGenerator();
        ProductGamma productGammaBack = getProductGammaRandomSampleGenerator();

        supplierGamma.addProducts(productGammaBack);
        assertThat(supplierGamma.getProducts()).containsOnly(productGammaBack);

        supplierGamma.removeProducts(productGammaBack);
        assertThat(supplierGamma.getProducts()).doesNotContain(productGammaBack);

        supplierGamma.products(new HashSet<>(Set.of(productGammaBack)));
        assertThat(supplierGamma.getProducts()).containsOnly(productGammaBack);

        supplierGamma.setProducts(new HashSet<>());
        assertThat(supplierGamma.getProducts()).doesNotContain(productGammaBack);
    }
}
