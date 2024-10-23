package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierSigmaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierSigma.class);
        SupplierSigma supplierSigma1 = getSupplierSigmaSample1();
        SupplierSigma supplierSigma2 = new SupplierSigma();
        assertThat(supplierSigma1).isNotEqualTo(supplierSigma2);

        supplierSigma2.setId(supplierSigma1.getId());
        assertThat(supplierSigma1).isEqualTo(supplierSigma2);

        supplierSigma2 = getSupplierSigmaSample2();
        assertThat(supplierSigma1).isNotEqualTo(supplierSigma2);
    }

    @Test
    void tenantTest() {
        SupplierSigma supplierSigma = getSupplierSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        supplierSigma.setTenant(masterTenantBack);
        assertThat(supplierSigma.getTenant()).isEqualTo(masterTenantBack);

        supplierSigma.tenant(null);
        assertThat(supplierSigma.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        SupplierSigma supplierSigma = getSupplierSigmaRandomSampleGenerator();
        ProductSigma productSigmaBack = getProductSigmaRandomSampleGenerator();

        supplierSigma.addProducts(productSigmaBack);
        assertThat(supplierSigma.getProducts()).containsOnly(productSigmaBack);

        supplierSigma.removeProducts(productSigmaBack);
        assertThat(supplierSigma.getProducts()).doesNotContain(productSigmaBack);

        supplierSigma.products(new HashSet<>(Set.of(productSigmaBack)));
        assertThat(supplierSigma.getProducts()).containsOnly(productSigmaBack);

        supplierSigma.setProducts(new HashSet<>());
        assertThat(supplierSigma.getProducts()).doesNotContain(productSigmaBack);
    }
}
