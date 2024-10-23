package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductViViTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierViViTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierViVi.class);
        NextSupplierViVi nextSupplierViVi1 = getNextSupplierViViSample1();
        NextSupplierViVi nextSupplierViVi2 = new NextSupplierViVi();
        assertThat(nextSupplierViVi1).isNotEqualTo(nextSupplierViVi2);

        nextSupplierViVi2.setId(nextSupplierViVi1.getId());
        assertThat(nextSupplierViVi1).isEqualTo(nextSupplierViVi2);

        nextSupplierViVi2 = getNextSupplierViViSample2();
        assertThat(nextSupplierViVi1).isNotEqualTo(nextSupplierViVi2);
    }

    @Test
    void tenantTest() {
        NextSupplierViVi nextSupplierViVi = getNextSupplierViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierViVi.setTenant(masterTenantBack);
        assertThat(nextSupplierViVi.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierViVi.tenant(null);
        assertThat(nextSupplierViVi.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierViVi nextSupplierViVi = getNextSupplierViViRandomSampleGenerator();
        NextProductViVi nextProductViViBack = getNextProductViViRandomSampleGenerator();

        nextSupplierViVi.addProducts(nextProductViViBack);
        assertThat(nextSupplierViVi.getProducts()).containsOnly(nextProductViViBack);

        nextSupplierViVi.removeProducts(nextProductViViBack);
        assertThat(nextSupplierViVi.getProducts()).doesNotContain(nextProductViViBack);

        nextSupplierViVi.products(new HashSet<>(Set.of(nextProductViViBack)));
        assertThat(nextSupplierViVi.getProducts()).containsOnly(nextProductViViBack);

        nextSupplierViVi.setProducts(new HashSet<>());
        assertThat(nextSupplierViVi.getProducts()).doesNotContain(nextProductViViBack);
    }
}
