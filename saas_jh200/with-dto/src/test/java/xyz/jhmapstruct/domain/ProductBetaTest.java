package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryBetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderBetaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductBetaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierBetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductBeta.class);
        ProductBeta productBeta1 = getProductBetaSample1();
        ProductBeta productBeta2 = new ProductBeta();
        assertThat(productBeta1).isNotEqualTo(productBeta2);

        productBeta2.setId(productBeta1.getId());
        assertThat(productBeta1).isEqualTo(productBeta2);

        productBeta2 = getProductBetaSample2();
        assertThat(productBeta1).isNotEqualTo(productBeta2);
    }

    @Test
    void categoryTest() {
        ProductBeta productBeta = getProductBetaRandomSampleGenerator();
        CategoryBeta categoryBetaBack = getCategoryBetaRandomSampleGenerator();

        productBeta.setCategory(categoryBetaBack);
        assertThat(productBeta.getCategory()).isEqualTo(categoryBetaBack);

        productBeta.category(null);
        assertThat(productBeta.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        ProductBeta productBeta = getProductBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        productBeta.setTenant(masterTenantBack);
        assertThat(productBeta.getTenant()).isEqualTo(masterTenantBack);

        productBeta.tenant(null);
        assertThat(productBeta.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        ProductBeta productBeta = getProductBetaRandomSampleGenerator();
        OrderBeta orderBetaBack = getOrderBetaRandomSampleGenerator();

        productBeta.setOrder(orderBetaBack);
        assertThat(productBeta.getOrder()).isEqualTo(orderBetaBack);

        productBeta.order(null);
        assertThat(productBeta.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductBeta productBeta = getProductBetaRandomSampleGenerator();
        SupplierBeta supplierBetaBack = getSupplierBetaRandomSampleGenerator();

        productBeta.addSuppliers(supplierBetaBack);
        assertThat(productBeta.getSuppliers()).containsOnly(supplierBetaBack);
        assertThat(supplierBetaBack.getProducts()).containsOnly(productBeta);

        productBeta.removeSuppliers(supplierBetaBack);
        assertThat(productBeta.getSuppliers()).doesNotContain(supplierBetaBack);
        assertThat(supplierBetaBack.getProducts()).doesNotContain(productBeta);

        productBeta.suppliers(new HashSet<>(Set.of(supplierBetaBack)));
        assertThat(productBeta.getSuppliers()).containsOnly(supplierBetaBack);
        assertThat(supplierBetaBack.getProducts()).containsOnly(productBeta);

        productBeta.setSuppliers(new HashSet<>());
        assertThat(productBeta.getSuppliers()).doesNotContain(supplierBetaBack);
        assertThat(supplierBetaBack.getProducts()).doesNotContain(productBeta);
    }
}
