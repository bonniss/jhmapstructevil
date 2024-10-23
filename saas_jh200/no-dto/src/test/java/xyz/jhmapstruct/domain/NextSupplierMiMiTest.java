package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierMiMiTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierMiMi.class);
        NextSupplierMiMi nextSupplierMiMi1 = getNextSupplierMiMiSample1();
        NextSupplierMiMi nextSupplierMiMi2 = new NextSupplierMiMi();
        assertThat(nextSupplierMiMi1).isNotEqualTo(nextSupplierMiMi2);

        nextSupplierMiMi2.setId(nextSupplierMiMi1.getId());
        assertThat(nextSupplierMiMi1).isEqualTo(nextSupplierMiMi2);

        nextSupplierMiMi2 = getNextSupplierMiMiSample2();
        assertThat(nextSupplierMiMi1).isNotEqualTo(nextSupplierMiMi2);
    }

    @Test
    void tenantTest() {
        NextSupplierMiMi nextSupplierMiMi = getNextSupplierMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierMiMi.setTenant(masterTenantBack);
        assertThat(nextSupplierMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierMiMi.tenant(null);
        assertThat(nextSupplierMiMi.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierMiMi nextSupplierMiMi = getNextSupplierMiMiRandomSampleGenerator();
        NextProductMiMi nextProductMiMiBack = getNextProductMiMiRandomSampleGenerator();

        nextSupplierMiMi.addProducts(nextProductMiMiBack);
        assertThat(nextSupplierMiMi.getProducts()).containsOnly(nextProductMiMiBack);

        nextSupplierMiMi.removeProducts(nextProductMiMiBack);
        assertThat(nextSupplierMiMi.getProducts()).doesNotContain(nextProductMiMiBack);

        nextSupplierMiMi.products(new HashSet<>(Set.of(nextProductMiMiBack)));
        assertThat(nextSupplierMiMi.getProducts()).containsOnly(nextProductMiMiBack);

        nextSupplierMiMi.setProducts(new HashSet<>());
        assertThat(nextSupplierMiMi.getProducts()).doesNotContain(nextProductMiMiBack);
    }
}
