package ai.realworld.domain;

import static ai.realworld.domain.AlAlexTypeViTestSamples.*;
import static ai.realworld.domain.AlBetonamuRelationViTestSamples.*;
import static ai.realworld.domain.AlGoreTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlBetonamuRelationViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBetonamuRelationVi.class);
        AlBetonamuRelationVi alBetonamuRelationVi1 = getAlBetonamuRelationViSample1();
        AlBetonamuRelationVi alBetonamuRelationVi2 = new AlBetonamuRelationVi();
        assertThat(alBetonamuRelationVi1).isNotEqualTo(alBetonamuRelationVi2);

        alBetonamuRelationVi2.setId(alBetonamuRelationVi1.getId());
        assertThat(alBetonamuRelationVi1).isEqualTo(alBetonamuRelationVi2);

        alBetonamuRelationVi2 = getAlBetonamuRelationViSample2();
        assertThat(alBetonamuRelationVi1).isNotEqualTo(alBetonamuRelationVi2);
    }

    @Test
    void supplierTest() {
        AlBetonamuRelationVi alBetonamuRelationVi = getAlBetonamuRelationViRandomSampleGenerator();
        AlAlexTypeVi alAlexTypeViBack = getAlAlexTypeViRandomSampleGenerator();

        alBetonamuRelationVi.setSupplier(alAlexTypeViBack);
        assertThat(alBetonamuRelationVi.getSupplier()).isEqualTo(alAlexTypeViBack);

        alBetonamuRelationVi.supplier(null);
        assertThat(alBetonamuRelationVi.getSupplier()).isNull();
    }

    @Test
    void customerTest() {
        AlBetonamuRelationVi alBetonamuRelationVi = getAlBetonamuRelationViRandomSampleGenerator();
        AlAlexTypeVi alAlexTypeViBack = getAlAlexTypeViRandomSampleGenerator();

        alBetonamuRelationVi.setCustomer(alAlexTypeViBack);
        assertThat(alBetonamuRelationVi.getCustomer()).isEqualTo(alAlexTypeViBack);

        alBetonamuRelationVi.customer(null);
        assertThat(alBetonamuRelationVi.getCustomer()).isNull();
    }

    @Test
    void applicationTest() {
        AlBetonamuRelationVi alBetonamuRelationVi = getAlBetonamuRelationViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alBetonamuRelationVi.setApplication(johnLennonBack);
        assertThat(alBetonamuRelationVi.getApplication()).isEqualTo(johnLennonBack);

        alBetonamuRelationVi.application(null);
        assertThat(alBetonamuRelationVi.getApplication()).isNull();
    }

    @Test
    void discountsTest() {
        AlBetonamuRelationVi alBetonamuRelationVi = getAlBetonamuRelationViRandomSampleGenerator();
        AlGore alGoreBack = getAlGoreRandomSampleGenerator();

        alBetonamuRelationVi.addDiscounts(alGoreBack);
        assertThat(alBetonamuRelationVi.getDiscounts()).containsOnly(alGoreBack);
        assertThat(alGoreBack.getBizRelationVi()).isEqualTo(alBetonamuRelationVi);

        alBetonamuRelationVi.removeDiscounts(alGoreBack);
        assertThat(alBetonamuRelationVi.getDiscounts()).doesNotContain(alGoreBack);
        assertThat(alGoreBack.getBizRelationVi()).isNull();

        alBetonamuRelationVi.discounts(new HashSet<>(Set.of(alGoreBack)));
        assertThat(alBetonamuRelationVi.getDiscounts()).containsOnly(alGoreBack);
        assertThat(alGoreBack.getBizRelationVi()).isEqualTo(alBetonamuRelationVi);

        alBetonamuRelationVi.setDiscounts(new HashSet<>());
        assertThat(alBetonamuRelationVi.getDiscounts()).doesNotContain(alGoreBack);
        assertThat(alGoreBack.getBizRelationVi()).isNull();
    }
}
