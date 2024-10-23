package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierSigmaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierSigma.class);
        NextSupplierSigma nextSupplierSigma1 = getNextSupplierSigmaSample1();
        NextSupplierSigma nextSupplierSigma2 = new NextSupplierSigma();
        assertThat(nextSupplierSigma1).isNotEqualTo(nextSupplierSigma2);

        nextSupplierSigma2.setId(nextSupplierSigma1.getId());
        assertThat(nextSupplierSigma1).isEqualTo(nextSupplierSigma2);

        nextSupplierSigma2 = getNextSupplierSigmaSample2();
        assertThat(nextSupplierSigma1).isNotEqualTo(nextSupplierSigma2);
    }

    @Test
    void tenantTest() {
        NextSupplierSigma nextSupplierSigma = getNextSupplierSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierSigma.setTenant(masterTenantBack);
        assertThat(nextSupplierSigma.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierSigma.tenant(null);
        assertThat(nextSupplierSigma.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierSigma nextSupplierSigma = getNextSupplierSigmaRandomSampleGenerator();
        NextProductSigma nextProductSigmaBack = getNextProductSigmaRandomSampleGenerator();

        nextSupplierSigma.addProducts(nextProductSigmaBack);
        assertThat(nextSupplierSigma.getProducts()).containsOnly(nextProductSigmaBack);

        nextSupplierSigma.removeProducts(nextProductSigmaBack);
        assertThat(nextSupplierSigma.getProducts()).doesNotContain(nextProductSigmaBack);

        nextSupplierSigma.products(new HashSet<>(Set.of(nextProductSigmaBack)));
        assertThat(nextSupplierSigma.getProducts()).containsOnly(nextProductSigmaBack);

        nextSupplierSigma.setProducts(new HashSet<>());
        assertThat(nextSupplierSigma.getProducts()).doesNotContain(nextProductSigmaBack);
    }
}
