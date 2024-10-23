package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.ProductThetaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierThetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierTheta.class);
        SupplierTheta supplierTheta1 = getSupplierThetaSample1();
        SupplierTheta supplierTheta2 = new SupplierTheta();
        assertThat(supplierTheta1).isNotEqualTo(supplierTheta2);

        supplierTheta2.setId(supplierTheta1.getId());
        assertThat(supplierTheta1).isEqualTo(supplierTheta2);

        supplierTheta2 = getSupplierThetaSample2();
        assertThat(supplierTheta1).isNotEqualTo(supplierTheta2);
    }

    @Test
    void tenantTest() {
        SupplierTheta supplierTheta = getSupplierThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        supplierTheta.setTenant(masterTenantBack);
        assertThat(supplierTheta.getTenant()).isEqualTo(masterTenantBack);

        supplierTheta.tenant(null);
        assertThat(supplierTheta.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        SupplierTheta supplierTheta = getSupplierThetaRandomSampleGenerator();
        ProductTheta productThetaBack = getProductThetaRandomSampleGenerator();

        supplierTheta.addProducts(productThetaBack);
        assertThat(supplierTheta.getProducts()).containsOnly(productThetaBack);

        supplierTheta.removeProducts(productThetaBack);
        assertThat(supplierTheta.getProducts()).doesNotContain(productThetaBack);

        supplierTheta.products(new HashSet<>(Set.of(productThetaBack)));
        assertThat(supplierTheta.getProducts()).containsOnly(productThetaBack);

        supplierTheta.setProducts(new HashSet<>());
        assertThat(supplierTheta.getProducts()).doesNotContain(productThetaBack);
    }
}
