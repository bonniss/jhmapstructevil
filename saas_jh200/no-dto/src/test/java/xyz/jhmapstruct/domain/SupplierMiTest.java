package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductMiTestSamples.*;
import static xyz.jhmapstruct.domain.SupplierMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierMi.class);
        SupplierMi supplierMi1 = getSupplierMiSample1();
        SupplierMi supplierMi2 = new SupplierMi();
        assertThat(supplierMi1).isNotEqualTo(supplierMi2);

        supplierMi2.setId(supplierMi1.getId());
        assertThat(supplierMi1).isEqualTo(supplierMi2);

        supplierMi2 = getSupplierMiSample2();
        assertThat(supplierMi1).isNotEqualTo(supplierMi2);
    }

    @Test
    void tenantTest() {
        SupplierMi supplierMi = getSupplierMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        supplierMi.setTenant(masterTenantBack);
        assertThat(supplierMi.getTenant()).isEqualTo(masterTenantBack);

        supplierMi.tenant(null);
        assertThat(supplierMi.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        SupplierMi supplierMi = getSupplierMiRandomSampleGenerator();
        NextProductMi nextProductMiBack = getNextProductMiRandomSampleGenerator();

        supplierMi.addProducts(nextProductMiBack);
        assertThat(supplierMi.getProducts()).containsOnly(nextProductMiBack);

        supplierMi.removeProducts(nextProductMiBack);
        assertThat(supplierMi.getProducts()).doesNotContain(nextProductMiBack);

        supplierMi.products(new HashSet<>(Set.of(nextProductMiBack)));
        assertThat(supplierMi.getProducts()).containsOnly(nextProductMiBack);

        supplierMi.setProducts(new HashSet<>());
        assertThat(supplierMi.getProducts()).doesNotContain(nextProductMiBack);
    }
}
