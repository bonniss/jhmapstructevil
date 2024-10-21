package ai.realworld.domain;

import static ai.realworld.domain.AlVueVueConditionTestSamples.*;
import static ai.realworld.domain.AlVueVueTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlVueVueConditionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueCondition.class);
        AlVueVueCondition alVueVueCondition1 = getAlVueVueConditionSample1();
        AlVueVueCondition alVueVueCondition2 = new AlVueVueCondition();
        assertThat(alVueVueCondition1).isNotEqualTo(alVueVueCondition2);

        alVueVueCondition2.setId(alVueVueCondition1.getId());
        assertThat(alVueVueCondition1).isEqualTo(alVueVueCondition2);

        alVueVueCondition2 = getAlVueVueConditionSample2();
        assertThat(alVueVueCondition1).isNotEqualTo(alVueVueCondition2);
    }

    @Test
    void parentTest() {
        AlVueVueCondition alVueVueCondition = getAlVueVueConditionRandomSampleGenerator();
        AlVueVue alVueVueBack = getAlVueVueRandomSampleGenerator();

        alVueVueCondition.setParent(alVueVueBack);
        assertThat(alVueVueCondition.getParent()).isEqualTo(alVueVueBack);

        alVueVueCondition.parent(null);
        assertThat(alVueVueCondition.getParent()).isNull();
    }

    @Test
    void applicationTest() {
        AlVueVueCondition alVueVueCondition = getAlVueVueConditionRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alVueVueCondition.setApplication(johnLennonBack);
        assertThat(alVueVueCondition.getApplication()).isEqualTo(johnLennonBack);

        alVueVueCondition.application(null);
        assertThat(alVueVueCondition.getApplication()).isNull();
    }
}
