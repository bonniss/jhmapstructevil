package ai.realworld.domain;

import static ai.realworld.domain.AlAlexTypeViTestSamples.*;
import static ai.realworld.domain.AlAppleViTestSamples.*;
import static ai.realworld.domain.AlBetonamuRelationViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlAlexTypeViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlAlexTypeVi.class);
        AlAlexTypeVi alAlexTypeVi1 = getAlAlexTypeViSample1();
        AlAlexTypeVi alAlexTypeVi2 = new AlAlexTypeVi();
        assertThat(alAlexTypeVi1).isNotEqualTo(alAlexTypeVi2);

        alAlexTypeVi2.setId(alAlexTypeVi1.getId());
        assertThat(alAlexTypeVi1).isEqualTo(alAlexTypeVi2);

        alAlexTypeVi2 = getAlAlexTypeViSample2();
        assertThat(alAlexTypeVi1).isNotEqualTo(alAlexTypeVi2);
    }

    @Test
    void applicationTest() {
        AlAlexTypeVi alAlexTypeVi = getAlAlexTypeViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alAlexTypeVi.setApplication(johnLennonBack);
        assertThat(alAlexTypeVi.getApplication()).isEqualTo(johnLennonBack);

        alAlexTypeVi.application(null);
        assertThat(alAlexTypeVi.getApplication()).isNull();
    }

    @Test
    void asSupplierTest() {
        AlAlexTypeVi alAlexTypeVi = getAlAlexTypeViRandomSampleGenerator();
        AlBetonamuRelationVi alBetonamuRelationViBack = getAlBetonamuRelationViRandomSampleGenerator();

        alAlexTypeVi.addAsSupplier(alBetonamuRelationViBack);
        assertThat(alAlexTypeVi.getAsSuppliers()).containsOnly(alBetonamuRelationViBack);
        assertThat(alBetonamuRelationViBack.getSupplier()).isEqualTo(alAlexTypeVi);

        alAlexTypeVi.removeAsSupplier(alBetonamuRelationViBack);
        assertThat(alAlexTypeVi.getAsSuppliers()).doesNotContain(alBetonamuRelationViBack);
        assertThat(alBetonamuRelationViBack.getSupplier()).isNull();

        alAlexTypeVi.asSuppliers(new HashSet<>(Set.of(alBetonamuRelationViBack)));
        assertThat(alAlexTypeVi.getAsSuppliers()).containsOnly(alBetonamuRelationViBack);
        assertThat(alBetonamuRelationViBack.getSupplier()).isEqualTo(alAlexTypeVi);

        alAlexTypeVi.setAsSuppliers(new HashSet<>());
        assertThat(alAlexTypeVi.getAsSuppliers()).doesNotContain(alBetonamuRelationViBack);
        assertThat(alBetonamuRelationViBack.getSupplier()).isNull();
    }

    @Test
    void asCustomerTest() {
        AlAlexTypeVi alAlexTypeVi = getAlAlexTypeViRandomSampleGenerator();
        AlBetonamuRelationVi alBetonamuRelationViBack = getAlBetonamuRelationViRandomSampleGenerator();

        alAlexTypeVi.addAsCustomer(alBetonamuRelationViBack);
        assertThat(alAlexTypeVi.getAsCustomers()).containsOnly(alBetonamuRelationViBack);
        assertThat(alBetonamuRelationViBack.getCustomer()).isEqualTo(alAlexTypeVi);

        alAlexTypeVi.removeAsCustomer(alBetonamuRelationViBack);
        assertThat(alAlexTypeVi.getAsCustomers()).doesNotContain(alBetonamuRelationViBack);
        assertThat(alBetonamuRelationViBack.getCustomer()).isNull();

        alAlexTypeVi.asCustomers(new HashSet<>(Set.of(alBetonamuRelationViBack)));
        assertThat(alAlexTypeVi.getAsCustomers()).containsOnly(alBetonamuRelationViBack);
        assertThat(alBetonamuRelationViBack.getCustomer()).isEqualTo(alAlexTypeVi);

        alAlexTypeVi.setAsCustomers(new HashSet<>());
        assertThat(alAlexTypeVi.getAsCustomers()).doesNotContain(alBetonamuRelationViBack);
        assertThat(alBetonamuRelationViBack.getCustomer()).isNull();
    }

    @Test
    void agenciesTest() {
        AlAlexTypeVi alAlexTypeVi = getAlAlexTypeViRandomSampleGenerator();
        AlAppleVi alAppleViBack = getAlAppleViRandomSampleGenerator();

        alAlexTypeVi.addAgencies(alAppleViBack);
        assertThat(alAlexTypeVi.getAgencies()).containsOnly(alAppleViBack);
        assertThat(alAppleViBack.getAgencyType()).isEqualTo(alAlexTypeVi);

        alAlexTypeVi.removeAgencies(alAppleViBack);
        assertThat(alAlexTypeVi.getAgencies()).doesNotContain(alAppleViBack);
        assertThat(alAppleViBack.getAgencyType()).isNull();

        alAlexTypeVi.agencies(new HashSet<>(Set.of(alAppleViBack)));
        assertThat(alAlexTypeVi.getAgencies()).containsOnly(alAppleViBack);
        assertThat(alAppleViBack.getAgencyType()).isEqualTo(alAlexTypeVi);

        alAlexTypeVi.setAgencies(new HashSet<>());
        assertThat(alAlexTypeVi.getAgencies()).doesNotContain(alAppleViBack);
        assertThat(alAppleViBack.getAgencyType()).isNull();
    }
}
