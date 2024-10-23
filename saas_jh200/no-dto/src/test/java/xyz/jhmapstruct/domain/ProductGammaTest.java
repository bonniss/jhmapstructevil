package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryGammaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.OrderGammaTestSamples.*;
import static xyz.jhmapstruct.domain.ProductGammaTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierGammaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductGamma.class);
        ProductGamma productGamma1 = getProductGammaSample1();
        ProductGamma productGamma2 = new ProductGamma();
        assertThat(productGamma1).isNotEqualTo(productGamma2);

        productGamma2.setId(productGamma1.getId());
        assertThat(productGamma1).isEqualTo(productGamma2);

        productGamma2 = getProductGammaSample2();
        assertThat(productGamma1).isNotEqualTo(productGamma2);
    }

    @Test
    void categoryTest() {
        ProductGamma productGamma = getProductGammaRandomSampleGenerator();
        CategoryGamma categoryGammaBack = getCategoryGammaRandomSampleGenerator();

        productGamma.setCategory(categoryGammaBack);
        assertThat(productGamma.getCategory()).isEqualTo(categoryGammaBack);

        productGamma.category(null);
        assertThat(productGamma.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        ProductGamma productGamma = getProductGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        productGamma.setTenant(masterTenantBack);
        assertThat(productGamma.getTenant()).isEqualTo(masterTenantBack);

        productGamma.tenant(null);
        assertThat(productGamma.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        ProductGamma productGamma = getProductGammaRandomSampleGenerator();
        OrderGamma orderGammaBack = getOrderGammaRandomSampleGenerator();

        productGamma.setOrder(orderGammaBack);
        assertThat(productGamma.getOrder()).isEqualTo(orderGammaBack);

        productGamma.order(null);
        assertThat(productGamma.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductGamma productGamma = getProductGammaRandomSampleGenerator();
        SupplierGamma supplierGammaBack = getSupplierGammaRandomSampleGenerator();

        productGamma.addSuppliers(supplierGammaBack);
        assertThat(productGamma.getSuppliers()).containsOnly(supplierGammaBack);
        assertThat(supplierGammaBack.getProducts()).containsOnly(productGamma);

        productGamma.removeSuppliers(supplierGammaBack);
        assertThat(productGamma.getSuppliers()).doesNotContain(supplierGammaBack);
        assertThat(supplierGammaBack.getProducts()).doesNotContain(productGamma);

        productGamma.suppliers(new HashSet<>(Set.of(supplierGammaBack)));
        assertThat(productGamma.getSuppliers()).containsOnly(supplierGammaBack);
        assertThat(supplierGammaBack.getProducts()).containsOnly(productGamma);

        productGamma.setSuppliers(new HashSet<>());
        assertThat(productGamma.getSuppliers()).doesNotContain(supplierGammaBack);
        assertThat(supplierGammaBack.getProducts()).doesNotContain(productGamma);
    }
}
