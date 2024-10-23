package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierAlphaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAlpha.class);
        ProductAlpha productAlpha1 = getProductAlphaSample1();
        ProductAlpha productAlpha2 = new ProductAlpha();
        assertThat(productAlpha1).isNotEqualTo(productAlpha2);

        productAlpha2.setId(productAlpha1.getId());
        assertThat(productAlpha1).isEqualTo(productAlpha2);

        productAlpha2 = getProductAlphaSample2();
        assertThat(productAlpha1).isNotEqualTo(productAlpha2);
    }

    @Test
    void categoryTest() {
        ProductAlpha productAlpha = getProductAlphaRandomSampleGenerator();
        CategoryAlpha categoryAlphaBack = getCategoryAlphaRandomSampleGenerator();

        productAlpha.setCategory(categoryAlphaBack);
        assertThat(productAlpha.getCategory()).isEqualTo(categoryAlphaBack);

        productAlpha.category(null);
        assertThat(productAlpha.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        ProductAlpha productAlpha = getProductAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        productAlpha.setTenant(masterTenantBack);
        assertThat(productAlpha.getTenant()).isEqualTo(masterTenantBack);

        productAlpha.tenant(null);
        assertThat(productAlpha.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        ProductAlpha productAlpha = getProductAlphaRandomSampleGenerator();
        OrderAlpha orderAlphaBack = getOrderAlphaRandomSampleGenerator();

        productAlpha.setOrder(orderAlphaBack);
        assertThat(productAlpha.getOrder()).isEqualTo(orderAlphaBack);

        productAlpha.order(null);
        assertThat(productAlpha.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductAlpha productAlpha = getProductAlphaRandomSampleGenerator();
        SupplierAlpha supplierAlphaBack = getSupplierAlphaRandomSampleGenerator();

        productAlpha.addSuppliers(supplierAlphaBack);
        assertThat(productAlpha.getSuppliers()).containsOnly(supplierAlphaBack);
        assertThat(supplierAlphaBack.getProducts()).containsOnly(productAlpha);

        productAlpha.removeSuppliers(supplierAlphaBack);
        assertThat(productAlpha.getSuppliers()).doesNotContain(supplierAlphaBack);
        assertThat(supplierAlphaBack.getProducts()).doesNotContain(productAlpha);

        productAlpha.suppliers(new HashSet<>(Set.of(supplierAlphaBack)));
        assertThat(productAlpha.getSuppliers()).containsOnly(supplierAlphaBack);
        assertThat(supplierAlphaBack.getProducts()).containsOnly(productAlpha);

        productAlpha.setSuppliers(new HashSet<>());
        assertThat(productAlpha.getSuppliers()).doesNotContain(supplierAlphaBack);
        assertThat(supplierAlphaBack.getProducts()).doesNotContain(productAlpha);
    }
}
