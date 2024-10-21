package ai.realworld.domain;

import static ai.realworld.domain.AlGoreConditionViTestSamples.*;
import static ai.realworld.domain.AlGoreTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoreConditionViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoreConditionVi.class);
        AlGoreConditionVi alGoreConditionVi1 = getAlGoreConditionViSample1();
        AlGoreConditionVi alGoreConditionVi2 = new AlGoreConditionVi();
        assertThat(alGoreConditionVi1).isNotEqualTo(alGoreConditionVi2);

        alGoreConditionVi2.setId(alGoreConditionVi1.getId());
        assertThat(alGoreConditionVi1).isEqualTo(alGoreConditionVi2);

        alGoreConditionVi2 = getAlGoreConditionViSample2();
        assertThat(alGoreConditionVi1).isNotEqualTo(alGoreConditionVi2);
    }

    @Test
    void parentTest() {
        AlGoreConditionVi alGoreConditionVi = getAlGoreConditionViRandomSampleGenerator();
        AlGore alGoreBack = getAlGoreRandomSampleGenerator();

        alGoreConditionVi.setParent(alGoreBack);
        assertThat(alGoreConditionVi.getParent()).isEqualTo(alGoreBack);

        alGoreConditionVi.parent(null);
        assertThat(alGoreConditionVi.getParent()).isNull();
    }

    @Test
    void applicationTest() {
        AlGoreConditionVi alGoreConditionVi = getAlGoreConditionViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alGoreConditionVi.setApplication(johnLennonBack);
        assertThat(alGoreConditionVi.getApplication()).isEqualTo(johnLennonBack);

        alGoreConditionVi.application(null);
        assertThat(alGoreConditionVi.getApplication()).isNull();
    }
}
