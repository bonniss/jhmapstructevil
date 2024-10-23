package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategorySigmaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierSigmaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductSigma.class);
        ProductSigma productSigma1 = getProductSigmaSample1();
        ProductSigma productSigma2 = new ProductSigma();
        assertThat(productSigma1).isNotEqualTo(productSigma2);

        productSigma2.setId(productSigma1.getId());
        assertThat(productSigma1).isEqualTo(productSigma2);

        productSigma2 = getProductSigmaSample2();
        assertThat(productSigma1).isNotEqualTo(productSigma2);
    }

    @Test
    void categoryTest() {
        ProductSigma productSigma = getProductSigmaRandomSampleGenerator();
        CategorySigma categorySigmaBack = getCategorySigmaRandomSampleGenerator();

        productSigma.setCategory(categorySigmaBack);
        assertThat(productSigma.getCategory()).isEqualTo(categorySigmaBack);

        productSigma.category(null);
        assertThat(productSigma.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        ProductSigma productSigma = getProductSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        productSigma.setTenant(masterTenantBack);
        assertThat(productSigma.getTenant()).isEqualTo(masterTenantBack);

        productSigma.tenant(null);
        assertThat(productSigma.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        ProductSigma productSigma = getProductSigmaRandomSampleGenerator();
        OrderSigma orderSigmaBack = getOrderSigmaRandomSampleGenerator();

        productSigma.setOrder(orderSigmaBack);
        assertThat(productSigma.getOrder()).isEqualTo(orderSigmaBack);

        productSigma.order(null);
        assertThat(productSigma.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductSigma productSigma = getProductSigmaRandomSampleGenerator();
        SupplierSigma supplierSigmaBack = getSupplierSigmaRandomSampleGenerator();

        productSigma.addSuppliers(supplierSigmaBack);
        assertThat(productSigma.getSuppliers()).containsOnly(supplierSigmaBack);
        assertThat(supplierSigmaBack.getProducts()).containsOnly(productSigma);

        productSigma.removeSuppliers(supplierSigmaBack);
        assertThat(productSigma.getSuppliers()).doesNotContain(supplierSigmaBack);
        assertThat(supplierSigmaBack.getProducts()).doesNotContain(productSigma);

        productSigma.suppliers(new HashSet<>(Set.of(supplierSigmaBack)));
        assertThat(productSigma.getSuppliers()).containsOnly(supplierSigmaBack);
        assertThat(supplierSigmaBack.getProducts()).containsOnly(productSigma);

        productSigma.setSuppliers(new HashSet<>());
        assertThat(productSigma.getSuppliers()).doesNotContain(supplierSigmaBack);
        assertThat(supplierSigmaBack.getProducts()).doesNotContain(productSigma);
    }
}
