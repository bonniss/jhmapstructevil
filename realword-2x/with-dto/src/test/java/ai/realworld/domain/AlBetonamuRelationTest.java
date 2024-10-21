package ai.realworld.domain;

import static ai.realworld.domain.AlAlexTypeTestSamples.*;
import static ai.realworld.domain.AlBetonamuRelationTestSamples.*;
import static ai.realworld.domain.AlGoreTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlBetonamuRelationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBetonamuRelation.class);
        AlBetonamuRelation alBetonamuRelation1 = getAlBetonamuRelationSample1();
        AlBetonamuRelation alBetonamuRelation2 = new AlBetonamuRelation();
        assertThat(alBetonamuRelation1).isNotEqualTo(alBetonamuRelation2);

        alBetonamuRelation2.setId(alBetonamuRelation1.getId());
        assertThat(alBetonamuRelation1).isEqualTo(alBetonamuRelation2);

        alBetonamuRelation2 = getAlBetonamuRelationSample2();
        assertThat(alBetonamuRelation1).isNotEqualTo(alBetonamuRelation2);
    }

    @Test
    void supplierTest() {
        AlBetonamuRelation alBetonamuRelation = getAlBetonamuRelationRandomSampleGenerator();
        AlAlexType alAlexTypeBack = getAlAlexTypeRandomSampleGenerator();

        alBetonamuRelation.setSupplier(alAlexTypeBack);
        assertThat(alBetonamuRelation.getSupplier()).isEqualTo(alAlexTypeBack);

        alBetonamuRelation.supplier(null);
        assertThat(alBetonamuRelation.getSupplier()).isNull();
    }

    @Test
    void customerTest() {
        AlBetonamuRelation alBetonamuRelation = getAlBetonamuRelationRandomSampleGenerator();
        AlAlexType alAlexTypeBack = getAlAlexTypeRandomSampleGenerator();

        alBetonamuRelation.setCustomer(alAlexTypeBack);
        assertThat(alBetonamuRelation.getCustomer()).isEqualTo(alAlexTypeBack);

        alBetonamuRelation.customer(null);
        assertThat(alBetonamuRelation.getCustomer()).isNull();
    }

    @Test
    void applicationTest() {
        AlBetonamuRelation alBetonamuRelation = getAlBetonamuRelationRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alBetonamuRelation.setApplication(johnLennonBack);
        assertThat(alBetonamuRelation.getApplication()).isEqualTo(johnLennonBack);

        alBetonamuRelation.application(null);
        assertThat(alBetonamuRelation.getApplication()).isNull();
    }

    @Test
    void discountsTest() {
        AlBetonamuRelation alBetonamuRelation = getAlBetonamuRelationRandomSampleGenerator();
        AlGore alGoreBack = getAlGoreRandomSampleGenerator();

        alBetonamuRelation.addDiscounts(alGoreBack);
        assertThat(alBetonamuRelation.getDiscounts()).containsOnly(alGoreBack);
        assertThat(alGoreBack.getBizRelation()).isEqualTo(alBetonamuRelation);

        alBetonamuRelation.removeDiscounts(alGoreBack);
        assertThat(alBetonamuRelation.getDiscounts()).doesNotContain(alGoreBack);
        assertThat(alGoreBack.getBizRelation()).isNull();

        alBetonamuRelation.discounts(new HashSet<>(Set.of(alGoreBack)));
        assertThat(alBetonamuRelation.getDiscounts()).containsOnly(alGoreBack);
        assertThat(alGoreBack.getBizRelation()).isEqualTo(alBetonamuRelation);

        alBetonamuRelation.setDiscounts(new HashSet<>());
        assertThat(alBetonamuRelation.getDiscounts()).doesNotContain(alGoreBack);
        assertThat(alGoreBack.getBizRelation()).isNull();
    }
}
