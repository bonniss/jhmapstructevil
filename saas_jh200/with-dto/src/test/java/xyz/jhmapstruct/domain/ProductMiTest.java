package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierMiTestSamples.*;
import static xyz.jhmapstruct.domain.OrderMiTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductMi.class);
        ProductMi productMi1 = getProductMiSample1();
        ProductMi productMi2 = new ProductMi();
        assertThat(productMi1).isNotEqualTo(productMi2);

        productMi2.setId(productMi1.getId());
        assertThat(productMi1).isEqualTo(productMi2);

        productMi2 = getProductMiSample2();
        assertThat(productMi1).isNotEqualTo(productMi2);
    }

    @Test
    void categoryTest() {
        ProductMi productMi = getProductMiRandomSampleGenerator();
        NextCategoryMi nextCategoryMiBack = getNextCategoryMiRandomSampleGenerator();

        productMi.setCategory(nextCategoryMiBack);
        assertThat(productMi.getCategory()).isEqualTo(nextCategoryMiBack);

        productMi.category(null);
        assertThat(productMi.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        ProductMi productMi = getProductMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        productMi.setTenant(masterTenantBack);
        assertThat(productMi.getTenant()).isEqualTo(masterTenantBack);

        productMi.tenant(null);
        assertThat(productMi.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        ProductMi productMi = getProductMiRandomSampleGenerator();
        OrderMi orderMiBack = getOrderMiRandomSampleGenerator();

        productMi.setOrder(orderMiBack);
        assertThat(productMi.getOrder()).isEqualTo(orderMiBack);

        productMi.order(null);
        assertThat(productMi.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        ProductMi productMi = getProductMiRandomSampleGenerator();
        NextSupplierMi nextSupplierMiBack = getNextSupplierMiRandomSampleGenerator();

        productMi.addSuppliers(nextSupplierMiBack);
        assertThat(productMi.getSuppliers()).containsOnly(nextSupplierMiBack);
        assertThat(nextSupplierMiBack.getProducts()).containsOnly(productMi);

        productMi.removeSuppliers(nextSupplierMiBack);
        assertThat(productMi.getSuppliers()).doesNotContain(nextSupplierMiBack);
        assertThat(nextSupplierMiBack.getProducts()).doesNotContain(productMi);

        productMi.suppliers(new HashSet<>(Set.of(nextSupplierMiBack)));
        assertThat(productMi.getSuppliers()).containsOnly(nextSupplierMiBack);
        assertThat(nextSupplierMiBack.getProducts()).containsOnly(productMi);

        productMi.setSuppliers(new HashSet<>());
        assertThat(productMi.getSuppliers()).doesNotContain(nextSupplierMiBack);
        assertThat(nextSupplierMiBack.getProducts()).doesNotContain(productMi);
    }
}
