package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryThetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderThetaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductThetaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierThetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductTheta.class);
        ProductTheta productTheta1 = getProductThetaSample1();
        ProductTheta productTheta2 = new ProductTheta();
        assertThat(productTheta1).isNotEqualTo(productTheta2);

        productTheta2.setId(productTheta1.getId());
        assertThat(productTheta1).isEqualTo(productTheta2);

        productTheta2 = getProductThetaSample2();
        assertThat(productTheta1).isNotEqualTo(productTheta2);
    }

    @Test
    void categoryTest() {
        ProductTheta productTheta = getProductThetaRandomSampleGenerator();
        CategoryTheta categoryThetaBack = getCategoryThetaRandomSampleGenerator();

        productTheta.setCategory(categoryThetaBack);
        assertThat(productTheta.getCategory()).isEqualTo(categoryThetaBack);

        productTheta.category(null);
        assertThat(productTheta.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        ProductTheta productTheta = getProductThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        productTheta.setTenant(masterTenantBack);
        assertThat(productTheta.getTenant()).isEqualTo(masterTenantBack);

        productTheta.tenant(null);
        assertThat(productTheta.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        ProductTheta productTheta = getProductThetaRandomSampleGenerator();
        OrderTheta orderThetaBack = getOrderThetaRandomSampleGenerator();

        productTheta.setOrder(orderThetaBack);
        assertThat(productTheta.getOrder()).isEqualTo(orderThetaBack);

        productTheta.order(null);
        assertThat(productTheta.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductTheta productTheta = getProductThetaRandomSampleGenerator();
        SupplierTheta supplierThetaBack = getSupplierThetaRandomSampleGenerator();

        productTheta.addSuppliers(supplierThetaBack);
        assertThat(productTheta.getSuppliers()).containsOnly(supplierThetaBack);
        assertThat(supplierThetaBack.getProducts()).containsOnly(productTheta);

        productTheta.removeSuppliers(supplierThetaBack);
        assertThat(productTheta.getSuppliers()).doesNotContain(supplierThetaBack);
        assertThat(supplierThetaBack.getProducts()).doesNotContain(productTheta);

        productTheta.suppliers(new HashSet<>(Set.of(supplierThetaBack)));
        assertThat(productTheta.getSuppliers()).containsOnly(supplierThetaBack);
        assertThat(supplierThetaBack.getProducts()).containsOnly(productTheta);

        productTheta.setSuppliers(new HashSet<>());
        assertThat(productTheta.getSuppliers()).doesNotContain(supplierThetaBack);
        assertThat(supplierThetaBack.getProducts()).doesNotContain(productTheta);
    }
}
