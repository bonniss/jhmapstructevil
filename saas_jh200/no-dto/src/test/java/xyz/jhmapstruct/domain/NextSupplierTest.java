package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplier.class);
        NextSupplier nextSupplier1 = getNextSupplierSample1();
        NextSupplier nextSupplier2 = new NextSupplier();
        assertThat(nextSupplier1).isNotEqualTo(nextSupplier2);

        nextSupplier2.setId(nextSupplier1.getId());
        assertThat(nextSupplier1).isEqualTo(nextSupplier2);

        nextSupplier2 = getNextSupplierSample2();
        assertThat(nextSupplier1).isNotEqualTo(nextSupplier2);
    }

    @Test
    void tenantTest() {
        NextSupplier nextSupplier = getNextSupplierRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplier.setTenant(masterTenantBack);
        assertThat(nextSupplier.getTenant()).isEqualTo(masterTenantBack);

        nextSupplier.tenant(null);
        assertThat(nextSupplier.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplier nextSupplier = getNextSupplierRandomSampleGenerator();
        NextProduct nextProductBack = getNextProductRandomSampleGenerator();

        nextSupplier.addProducts(nextProductBack);
        assertThat(nextSupplier.getProducts()).containsOnly(nextProductBack);

        nextSupplier.removeProducts(nextProductBack);
        assertThat(nextSupplier.getProducts()).doesNotContain(nextProductBack);

        nextSupplier.products(new HashSet<>(Set.of(nextProductBack)));
        assertThat(nextSupplier.getProducts()).containsOnly(nextProductBack);

        nextSupplier.setProducts(new HashSet<>());
        assertThat(nextSupplier.getProducts()).doesNotContain(nextProductBack);
    }
}
