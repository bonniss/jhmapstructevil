package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductGammaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierGammaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierGamma.class);
        NextSupplierGamma nextSupplierGamma1 = getNextSupplierGammaSample1();
        NextSupplierGamma nextSupplierGamma2 = new NextSupplierGamma();
        assertThat(nextSupplierGamma1).isNotEqualTo(nextSupplierGamma2);

        nextSupplierGamma2.setId(nextSupplierGamma1.getId());
        assertThat(nextSupplierGamma1).isEqualTo(nextSupplierGamma2);

        nextSupplierGamma2 = getNextSupplierGammaSample2();
        assertThat(nextSupplierGamma1).isNotEqualTo(nextSupplierGamma2);
    }

    @Test
    void tenantTest() {
        NextSupplierGamma nextSupplierGamma = getNextSupplierGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierGamma.setTenant(masterTenantBack);
        assertThat(nextSupplierGamma.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierGamma.tenant(null);
        assertThat(nextSupplierGamma.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierGamma nextSupplierGamma = getNextSupplierGammaRandomSampleGenerator();
        NextProductGamma nextProductGammaBack = getNextProductGammaRandomSampleGenerator();

        nextSupplierGamma.addProducts(nextProductGammaBack);
        assertThat(nextSupplierGamma.getProducts()).containsOnly(nextProductGammaBack);

        nextSupplierGamma.removeProducts(nextProductGammaBack);
        assertThat(nextSupplierGamma.getProducts()).doesNotContain(nextProductGammaBack);

        nextSupplierGamma.products(new HashSet<>(Set.of(nextProductGammaBack)));
        assertThat(nextSupplierGamma.getProducts()).containsOnly(nextProductGammaBack);

        nextSupplierGamma.setProducts(new HashSet<>());
        assertThat(nextSupplierGamma.getProducts()).doesNotContain(nextProductGammaBack);
    }
}
