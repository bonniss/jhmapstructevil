package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextCategoryThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextOrderThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextProductThetaTestSamples.*;
import static xyz.jhmapstruct.domain.NextSupplierThetaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductTheta.class);
        NextProductTheta nextProductTheta1 = getNextProductThetaSample1();
        NextProductTheta nextProductTheta2 = new NextProductTheta();
        assertThat(nextProductTheta1).isNotEqualTo(nextProductTheta2);

        nextProductTheta2.setId(nextProductTheta1.getId());
        assertThat(nextProductTheta1).isEqualTo(nextProductTheta2);

        nextProductTheta2 = getNextProductThetaSample2();
        assertThat(nextProductTheta1).isNotEqualTo(nextProductTheta2);
    }

    @Test
    void categoryTest() {
        NextProductTheta nextProductTheta = getNextProductThetaRandomSampleGenerator();
        NextCategoryTheta nextCategoryThetaBack = getNextCategoryThetaRandomSampleGenerator();

        nextProductTheta.setCategory(nextCategoryThetaBack);
        assertThat(nextProductTheta.getCategory()).isEqualTo(nextCategoryThetaBack);

        nextProductTheta.category(null);
        assertThat(nextProductTheta.getCategory()).isNull();
    }

    @Test
    void tenantTest() {
        NextProductTheta nextProductTheta = getNextProductThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextProductTheta.setTenant(masterTenantBack);
        assertThat(nextProductTheta.getTenant()).isEqualTo(masterTenantBack);

        nextProductTheta.tenant(null);
        assertThat(nextProductTheta.getTenant()).isNull();
    }

    @Test
    void orderTest() {
        NextProductTheta nextProductTheta = getNextProductThetaRandomSampleGenerator();
        NextOrderTheta nextOrderThetaBack = getNextOrderThetaRandomSampleGenerator();

        nextProductTheta.setOrder(nextOrderThetaBack);
        assertThat(nextProductTheta.getOrder()).isEqualTo(nextOrderThetaBack);

        nextProductTheta.order(null);
        assertThat(nextProductTheta.getOrder()).isNull();
    }

    @Test
    void suppliersTest() {
        NextProductTheta nextProductTheta = getNextProductThetaRandomSampleGenerator();
        NextSupplierTheta nextSupplierThetaBack = getNextSupplierThetaRandomSampleGenerator();

        nextProductTheta.addSuppliers(nextSupplierThetaBack);
        assertThat(nextProductTheta.getSuppliers()).containsOnly(nextSupplierThetaBack);
        assertThat(nextSupplierThetaBack.getProducts()).containsOnly(nextProductTheta);

        nextProductTheta.removeSuppliers(nextSupplierThetaBack);
        assertThat(nextProductTheta.getSuppliers()).doesNotContain(nextSupplierThetaBack);
        assertThat(nextSupplierThetaBack.getProducts()).doesNotContain(nextProductTheta);

        nextProductTheta.suppliers(new HashSet<>(Set.of(nextSupplierThetaBack)));
        assertThat(nextProductTheta.getSuppliers()).containsOnly(nextSupplierThetaBack);
        assertThat(nextSupplierThetaBack.getProducts()).containsOnly(nextProductTheta);

        nextProductTheta.setSuppliers(new HashSet<>());
        assertThat(nextProductTheta.getSuppliers()).doesNotContain(nextSupplierThetaBack);
        assertThat(nextSupplierThetaBack.getProducts()).doesNotContain(nextProductTheta);
    }
}
