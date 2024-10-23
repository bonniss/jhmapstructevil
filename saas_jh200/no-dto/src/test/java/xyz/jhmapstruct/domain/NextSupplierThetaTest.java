package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierThetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierTheta.class);
        NextSupplierTheta nextSupplierTheta1 = getNextSupplierThetaSample1();
        NextSupplierTheta nextSupplierTheta2 = new NextSupplierTheta();
        assertThat(nextSupplierTheta1).isNotEqualTo(nextSupplierTheta2);

        nextSupplierTheta2.setId(nextSupplierTheta1.getId());
        assertThat(nextSupplierTheta1).isEqualTo(nextSupplierTheta2);

        nextSupplierTheta2 = getNextSupplierThetaSample2();
        assertThat(nextSupplierTheta1).isNotEqualTo(nextSupplierTheta2);
    }

    @Test
    void tenantTest() {
        NextSupplierTheta nextSupplierTheta = getNextSupplierThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextSupplierTheta.setTenant(masterTenantBack);
        assertThat(nextSupplierTheta.getTenant()).isEqualTo(masterTenantBack);

        nextSupplierTheta.tenant(null);
        assertThat(nextSupplierTheta.getTenant()).isNull();
    }

    @Test
    void productsTest() {
        NextSupplierTheta nextSupplierTheta = getNextSupplierThetaRandomSampleGenerator();
        NextProductTheta nextProductThetaBack = getNextProductThetaRandomSampleGenerator();

        nextSupplierTheta.addProducts(nextProductThetaBack);
        assertThat(nextSupplierTheta.getProducts()).containsOnly(nextProductThetaBack);

        nextSupplierTheta.removeProducts(nextProductThetaBack);
        assertThat(nextSupplierTheta.getProducts()).doesNotContain(nextProductThetaBack);

        nextSupplierTheta.products(new HashSet<>(Set.of(nextProductThetaBack)));
        assertThat(nextSupplierTheta.getProducts()).containsOnly(nextProductThetaBack);

        nextSupplierTheta.setProducts(new HashSet<>());
        assertThat(nextSupplierTheta.getProducts()).doesNotContain(nextProductThetaBack);
    }
}
