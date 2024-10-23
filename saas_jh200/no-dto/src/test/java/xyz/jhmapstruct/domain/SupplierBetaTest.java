package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductBetaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierBetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierBeta.class);
        SupplierBeta supplierBeta1 = getSupplierBetaSample1();
        SupplierBeta supplierBeta2 = new SupplierBeta();
        assertThat(supplierBeta1).isNotEqualTo(supplierBeta2);

        supplierBeta2.setId(supplierBeta1.getId());
        assertThat(supplierBeta1).isEqualTo(supplierBeta2);

        supplierBeta2 = getSupplierBetaSample2();
        assertThat(supplierBeta1).isNotEqualTo(supplierBeta2);
    }

    @Test
    void tenantTest() {
        SupplierBeta supplierBeta = getSupplierBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        supplierBeta.setTenant(masterTenantBack);
        assertThat(supplierBeta.getTenant()).isEqualTo(masterTenantBack);

        supplierBeta.tenant(null);
        assertThat(supplierBeta.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        SupplierBeta supplierBeta = getSupplierBetaRandomSampleGenerator();
        ProductBeta productBetaBack = getProductBetaRandomSampleGenerator();

        supplierBeta.addProducts(productBetaBack);
        assertThat(supplierBeta.getProducts()).containsOnly(productBetaBack);

        supplierBeta.removeProducts(productBetaBack);
        assertThat(supplierBeta.getProducts()).doesNotContain(productBetaBack);

        supplierBeta.products(new HashSet<>(Set.of(productBetaBack)));
        assertThat(supplierBeta.getProducts()).containsOnly(productBetaBack);

        supplierBeta.setProducts(new HashSet<>());
        assertThat(supplierBeta.getProducts()).doesNotContain(productBetaBack);
    }
}
