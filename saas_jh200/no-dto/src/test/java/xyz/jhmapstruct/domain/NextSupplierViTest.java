package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductViTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierVi.class);
        NextSupplierVi nextSupplierVi1 = getNextSupplierViSample1();
        NextSupplierVi nextSupplierVi2 = new NextSupplierVi();
        assertThat(nextSupplierVi1).isNotEqualTo(nextSupplierVi2);

        nextSupplierVi2.setId(nextSupplierVi1.getId());
        assertThat(nextSupplierVi1).isEqualTo(nextSupplierVi2);

        nextSupplierVi2 = getNextSupplierViSample2();
        assertThat(nextSupplierVi1).isNotEqualTo(nextSupplierVi2);
    }

    @Test
    void tenantTest() {
        NextSupplierVi nextSupplierVi = getNextSupplierViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierVi.setTenant(masterTenantBack);
        assertThat(nextSupplierVi.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierVi.tenant(null);
        assertThat(nextSupplierVi.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierVi nextSupplierVi = getNextSupplierViRandomSampleGenerator();
        NextProductVi nextProductViBack = getNextProductViRandomSampleGenerator();

        nextSupplierVi.addProducts(nextProductViBack);
        assertThat(nextSupplierVi.getProducts()).containsOnly(nextProductViBack);

        nextSupplierVi.removeProducts(nextProductViBack);
        assertThat(nextSupplierVi.getProducts()).doesNotContain(nextProductViBack);

        nextSupplierVi.products(new HashSet<>(Set.of(nextProductViBack)));
        assertThat(nextSupplierVi.getProducts()).containsOnly(nextProductViBack);

        nextSupplierVi.setProducts(new HashSet<>());
        assertThat(nextSupplierVi.getProducts()).doesNotContain(nextProductViBack);
    }
}
