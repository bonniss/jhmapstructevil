package ai.realworld.domain;

import static ai.realworld.domain.AlVueVueViConditionTestSamples.*;
import static ai.realworld.domain.AlVueVueViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlVueVueViConditionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueViCondition.class);
        AlVueVueViCondition alVueVueViCondition1 = getAlVueVueViConditionSample1();
        AlVueVueViCondition alVueVueViCondition2 = new AlVueVueViCondition();
        assertThat(alVueVueViCondition1).isNotEqualTo(alVueVueViCondition2);

        alVueVueViCondition2.setId(alVueVueViCondition1.getId());
        assertThat(alVueVueViCondition1).isEqualTo(alVueVueViCondition2);

        alVueVueViCondition2 = getAlVueVueViConditionSample2();
        assertThat(alVueVueViCondition1).isNotEqualTo(alVueVueViCondition2);
    }

    @Test
    void parentTest() {
        AlVueVueViCondition alVueVueViCondition = getAlVueVueViConditionRandomSampleGenerator();
        AlVueVueVi alVueVueViBack = getAlVueVueViRandomSampleGenerator();

        alVueVueViCondition.setParent(alVueVueViBack);
        assertThat(alVueVueViCondition.getParent()).isEqualTo(alVueVueViBack);

        alVueVueViCondition.parent(null);
        assertThat(alVueVueViCondition.getParent()).isNull();
    }

    @Test
    void applicationTest() {
        AlVueVueViCondition alVueVueViCondition = getAlVueVueViConditionRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alVueVueViCondition.setApplication(johnLennonBack);
        assertThat(alVueVueViCondition.getApplication()).isEqualTo(johnLennonBack);

        alVueVueViCondition.application(null);
        assertThat(alVueVueViCondition.getApplication()).isNull();
    }
}
