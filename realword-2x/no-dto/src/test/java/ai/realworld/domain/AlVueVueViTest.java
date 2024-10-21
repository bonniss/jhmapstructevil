package ai.realworld.domain;

import static ai.realworld.domain.AlVueVueViConditionTestSamples.*;
import static ai.realworld.domain.AlVueVueViTestSamples.*;
import static ai.realworld.domain.AlVueVueViUsageTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlVueVueViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueVi.class);
        AlVueVueVi alVueVueVi1 = getAlVueVueViSample1();
        AlVueVueVi alVueVueVi2 = new AlVueVueVi();
        assertThat(alVueVueVi1).isNotEqualTo(alVueVueVi2);

        alVueVueVi2.setId(alVueVueVi1.getId());
        assertThat(alVueVueVi1).isEqualTo(alVueVueVi2);

        alVueVueVi2 = getAlVueVueViSample2();
        assertThat(alVueVueVi1).isNotEqualTo(alVueVueVi2);
    }

    @Test
    void imageTest() {
        AlVueVueVi alVueVueVi = getAlVueVueViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alVueVueVi.setImage(metaverseBack);
        assertThat(alVueVueVi.getImage()).isEqualTo(metaverseBack);

        alVueVueVi.image(null);
        assertThat(alVueVueVi.getImage()).isNull();
    }

    @Test
    void alVueVueViUsageTest() {
        AlVueVueVi alVueVueVi = getAlVueVueViRandomSampleGenerator();
        AlVueVueViUsage alVueVueViUsageBack = getAlVueVueViUsageRandomSampleGenerator();

        alVueVueVi.setAlVueVueViUsage(alVueVueViUsageBack);
        assertThat(alVueVueVi.getAlVueVueViUsage()).isEqualTo(alVueVueViUsageBack);

        alVueVueVi.alVueVueViUsage(null);
        assertThat(alVueVueVi.getAlVueVueViUsage()).isNull();
    }

    @Test
    void applicationTest() {
        AlVueVueVi alVueVueVi = getAlVueVueViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alVueVueVi.setApplication(johnLennonBack);
        assertThat(alVueVueVi.getApplication()).isEqualTo(johnLennonBack);

        alVueVueVi.application(null);
        assertThat(alVueVueVi.getApplication()).isNull();
    }

    @Test
    void conditionsTest() {
        AlVueVueVi alVueVueVi = getAlVueVueViRandomSampleGenerator();
        AlVueVueViCondition alVueVueViConditionBack = getAlVueVueViConditionRandomSampleGenerator();

        alVueVueVi.addConditions(alVueVueViConditionBack);
        assertThat(alVueVueVi.getConditions()).containsOnly(alVueVueViConditionBack);
        assertThat(alVueVueViConditionBack.getParent()).isEqualTo(alVueVueVi);

        alVueVueVi.removeConditions(alVueVueViConditionBack);
        assertThat(alVueVueVi.getConditions()).doesNotContain(alVueVueViConditionBack);
        assertThat(alVueVueViConditionBack.getParent()).isNull();

        alVueVueVi.conditions(new HashSet<>(Set.of(alVueVueViConditionBack)));
        assertThat(alVueVueVi.getConditions()).containsOnly(alVueVueViConditionBack);
        assertThat(alVueVueViConditionBack.getParent()).isEqualTo(alVueVueVi);

        alVueVueVi.setConditions(new HashSet<>());
        assertThat(alVueVueVi.getConditions()).doesNotContain(alVueVueViConditionBack);
        assertThat(alVueVueViConditionBack.getParent()).isNull();
    }
}
