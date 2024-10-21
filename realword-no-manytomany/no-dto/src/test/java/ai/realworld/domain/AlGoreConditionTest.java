package ai.realworld.domain;

import static ai.realworld.domain.AlGoreConditionTestSamples.*;
import static ai.realworld.domain.AlGoreTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoreConditionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoreCondition.class);
        AlGoreCondition alGoreCondition1 = getAlGoreConditionSample1();
        AlGoreCondition alGoreCondition2 = new AlGoreCondition();
        assertThat(alGoreCondition1).isNotEqualTo(alGoreCondition2);

        alGoreCondition2.setId(alGoreCondition1.getId());
        assertThat(alGoreCondition1).isEqualTo(alGoreCondition2);

        alGoreCondition2 = getAlGoreConditionSample2();
        assertThat(alGoreCondition1).isNotEqualTo(alGoreCondition2);
    }

    @Test
    void parentTest() {
        AlGoreCondition alGoreCondition = getAlGoreConditionRandomSampleGenerator();
        AlGore alGoreBack = getAlGoreRandomSampleGenerator();

        alGoreCondition.setParent(alGoreBack);
        assertThat(alGoreCondition.getParent()).isEqualTo(alGoreBack);

        alGoreCondition.parent(null);
        assertThat(alGoreCondition.getParent()).isNull();
    }

    @Test
    void applicationTest() {
        AlGoreCondition alGoreCondition = getAlGoreConditionRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alGoreCondition.setApplication(johnLennonBack);
        assertThat(alGoreCondition.getApplication()).isEqualTo(johnLennonBack);

        alGoreCondition.application(null);
        assertThat(alGoreCondition.getApplication()).isNull();
    }
}
