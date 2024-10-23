package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierMiTestSamples.*;
import static xyz.jhmapstruct.domain.ProductMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierMi.class);
        NextSupplierMi nextSupplierMi1 = getNextSupplierMiSample1();
        NextSupplierMi nextSupplierMi2 = new NextSupplierMi();
        assertThat(nextSupplierMi1).isNotEqualTo(nextSupplierMi2);

        nextSupplierMi2.setId(nextSupplierMi1.getId());
        assertThat(nextSupplierMi1).isEqualTo(nextSupplierMi2);

        nextSupplierMi2 = getNextSupplierMiSample2();
        assertThat(nextSupplierMi1).isNotEqualTo(nextSupplierMi2);
    }

    @Test
    void tenantTest() {
        NextSupplierMi nextSupplierMi = getNextSupplierMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierMi.setTenant(masterTenantBack);
        assertThat(nextSupplierMi.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierMi.tenant(null);
        assertThat(nextSupplierMi.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierMi nextSupplierMi = getNextSupplierMiRandomSampleGenerator();
        ProductMi productMiBack = getProductMiRandomSampleGenerator();

        nextSupplierMi.addProducts(productMiBack);
        assertThat(nextSupplierMi.getProducts()).containsOnly(productMiBack);

        nextSupplierMi.removeProducts(productMiBack);
        assertThat(nextSupplierMi.getProducts()).doesNotContain(productMiBack);

        nextSupplierMi.products(new HashSet<>(Set.of(productMiBack)));
        assertThat(nextSupplierMi.getProducts()).containsOnly(productMiBack);

        nextSupplierMi.setProducts(new HashSet<>());
        assertThat(nextSupplierMi.getProducts()).doesNotContain(productMiBack);
    }
}
