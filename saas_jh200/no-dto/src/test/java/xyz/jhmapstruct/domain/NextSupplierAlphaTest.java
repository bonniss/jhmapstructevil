package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierAlphaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierAlpha.class);
        NextSupplierAlpha nextSupplierAlpha1 = getNextSupplierAlphaSample1();
        NextSupplierAlpha nextSupplierAlpha2 = new NextSupplierAlpha();
        assertThat(nextSupplierAlpha1).isNotEqualTo(nextSupplierAlpha2);

        nextSupplierAlpha2.setId(nextSupplierAlpha1.getId());
        assertThat(nextSupplierAlpha1).isEqualTo(nextSupplierAlpha2);

        nextSupplierAlpha2 = getNextSupplierAlphaSample2();
        assertThat(nextSupplierAlpha1).isNotEqualTo(nextSupplierAlpha2);
    }

    @Test
    void tenantTest() {
        NextSupplierAlpha nextSupplierAlpha = getNextSupplierAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierAlpha.setTenant(masterTenantBack);
        assertThat(nextSupplierAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierAlpha.tenant(null);
        assertThat(nextSupplierAlpha.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierAlpha nextSupplierAlpha = getNextSupplierAlphaRandomSampleGenerator();
        NextProductAlpha nextProductAlphaBack = getNextProductAlphaRandomSampleGenerator();

        nextSupplierAlpha.addProducts(nextProductAlphaBack);
        assertThat(nextSupplierAlpha.getProducts()).containsOnly(nextProductAlphaBack);

        nextSupplierAlpha.removeProducts(nextProductAlphaBack);
        assertThat(nextSupplierAlpha.getProducts()).doesNotContain(nextProductAlphaBack);

        nextSupplierAlpha.products(new HashSet<>(Set.of(nextProductAlphaBack)));
        assertThat(nextSupplierAlpha.getProducts()).containsOnly(nextProductAlphaBack);

        nextSupplierAlpha.setProducts(new HashSet<>());
        assertThat(nextSupplierAlpha.getProducts()).doesNotContain(nextProductAlphaBack);
    }
}
