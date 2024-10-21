package ai.realworld.domain;

import static ai.realworld.domain.AlAlexTypeTestSamples.*;
import static ai.realworld.domain.AlAppleTestSamples.*;
import static ai.realworld.domain.AlBetonamuRelationTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlAlexTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlAlexType.class);
        AlAlexType alAlexType1 = getAlAlexTypeSample1();
        AlAlexType alAlexType2 = new AlAlexType();
        assertThat(alAlexType1).isNotEqualTo(alAlexType2);

        alAlexType2.setId(alAlexType1.getId());
        assertThat(alAlexType1).isEqualTo(alAlexType2);

        alAlexType2 = getAlAlexTypeSample2();
        assertThat(alAlexType1).isNotEqualTo(alAlexType2);
    }

    @Test
    void applicationTest() {
        AlAlexType alAlexType = getAlAlexTypeRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alAlexType.setApplication(johnLennonBack);
        assertThat(alAlexType.getApplication()).isEqualTo(johnLennonBack);

        alAlexType.application(null);
        assertThat(alAlexType.getApplication()).isNull();
    }

    @Test
    void asSupplierTest() {
        AlAlexType alAlexType = getAlAlexTypeRandomSampleGenerator();
        AlBetonamuRelation alBetonamuRelationBack = getAlBetonamuRelationRandomSampleGenerator();

        alAlexType.addAsSupplier(alBetonamuRelationBack);
        assertThat(alAlexType.getAsSuppliers()).containsOnly(alBetonamuRelationBack);
        assertThat(alBetonamuRelationBack.getSupplier()).isEqualTo(alAlexType);

        alAlexType.removeAsSupplier(alBetonamuRelationBack);
        assertThat(alAlexType.getAsSuppliers()).doesNotContain(alBetonamuRelationBack);
        assertThat(alBetonamuRelationBack.getSupplier()).isNull();

        alAlexType.asSuppliers(new HashSet<>(Set.of(alBetonamuRelationBack)));
        assertThat(alAlexType.getAsSuppliers()).containsOnly(alBetonamuRelationBack);
        assertThat(alBetonamuRelationBack.getSupplier()).isEqualTo(alAlexType);

        alAlexType.setAsSuppliers(new HashSet<>());
        assertThat(alAlexType.getAsSuppliers()).doesNotContain(alBetonamuRelationBack);
        assertThat(alBetonamuRelationBack.getSupplier()).isNull();
    }

    @Test
    void asCustomerTest() {
        AlAlexType alAlexType = getAlAlexTypeRandomSampleGenerator();
        AlBetonamuRelation alBetonamuRelationBack = getAlBetonamuRelationRandomSampleGenerator();

        alAlexType.addAsCustomer(alBetonamuRelationBack);
        assertThat(alAlexType.getAsCustomers()).containsOnly(alBetonamuRelationBack);
        assertThat(alBetonamuRelationBack.getCustomer()).isEqualTo(alAlexType);

        alAlexType.removeAsCustomer(alBetonamuRelationBack);
        assertThat(alAlexType.getAsCustomers()).doesNotContain(alBetonamuRelationBack);
        assertThat(alBetonamuRelationBack.getCustomer()).isNull();

        alAlexType.asCustomers(new HashSet<>(Set.of(alBetonamuRelationBack)));
        assertThat(alAlexType.getAsCustomers()).containsOnly(alBetonamuRelationBack);
        assertThat(alBetonamuRelationBack.getCustomer()).isEqualTo(alAlexType);

        alAlexType.setAsCustomers(new HashSet<>());
        assertThat(alAlexType.getAsCustomers()).doesNotContain(alBetonamuRelationBack);
        assertThat(alBetonamuRelationBack.getCustomer()).isNull();
    }

    @Test
    void agenciesTest() {
        AlAlexType alAlexType = getAlAlexTypeRandomSampleGenerator();
        AlApple alAppleBack = getAlAppleRandomSampleGenerator();

        alAlexType.addAgencies(alAppleBack);
        assertThat(alAlexType.getAgencies()).containsOnly(alAppleBack);
        assertThat(alAppleBack.getAgencyType()).isEqualTo(alAlexType);

        alAlexType.removeAgencies(alAppleBack);
        assertThat(alAlexType.getAgencies()).doesNotContain(alAppleBack);
        assertThat(alAppleBack.getAgencyType()).isNull();

        alAlexType.agencies(new HashSet<>(Set.of(alAppleBack)));
        assertThat(alAlexType.getAgencies()).containsOnly(alAppleBack);
        assertThat(alAppleBack.getAgencyType()).isEqualTo(alAlexType);

        alAlexType.setAgencies(new HashSet<>());
        assertThat(alAlexType.getAgencies()).doesNotContain(alAppleBack);
        assertThat(alAppleBack.getAgencyType()).isNull();
    }
}
