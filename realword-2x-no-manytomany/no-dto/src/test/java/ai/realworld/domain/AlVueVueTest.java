package ai.realworld.domain;

import static ai.realworld.domain.AlVueVueConditionTestSamples.*;
import static ai.realworld.domain.AlVueVueTestSamples.*;
import static ai.realworld.domain.AlVueVueUsageTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlVueVueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVue.class);
        AlVueVue alVueVue1 = getAlVueVueSample1();
        AlVueVue alVueVue2 = new AlVueVue();
        assertThat(alVueVue1).isNotEqualTo(alVueVue2);

        alVueVue2.setId(alVueVue1.getId());
        assertThat(alVueVue1).isEqualTo(alVueVue2);

        alVueVue2 = getAlVueVueSample2();
        assertThat(alVueVue1).isNotEqualTo(alVueVue2);
    }

    @Test
    void imageTest() {
        AlVueVue alVueVue = getAlVueVueRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alVueVue.setImage(metaverseBack);
        assertThat(alVueVue.getImage()).isEqualTo(metaverseBack);

        alVueVue.image(null);
        assertThat(alVueVue.getImage()).isNull();
    }

    @Test
    void alVueVueUsageTest() {
        AlVueVue alVueVue = getAlVueVueRandomSampleGenerator();
        AlVueVueUsage alVueVueUsageBack = getAlVueVueUsageRandomSampleGenerator();

        alVueVue.setAlVueVueUsage(alVueVueUsageBack);
        assertThat(alVueVue.getAlVueVueUsage()).isEqualTo(alVueVueUsageBack);

        alVueVue.alVueVueUsage(null);
        assertThat(alVueVue.getAlVueVueUsage()).isNull();
    }

    @Test
    void applicationTest() {
        AlVueVue alVueVue = getAlVueVueRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alVueVue.setApplication(johnLennonBack);
        assertThat(alVueVue.getApplication()).isEqualTo(johnLennonBack);

        alVueVue.application(null);
        assertThat(alVueVue.getApplication()).isNull();
    }

    @Test
    void conditionsTest() {
        AlVueVue alVueVue = getAlVueVueRandomSampleGenerator();
        AlVueVueCondition alVueVueConditionBack = getAlVueVueConditionRandomSampleGenerator();

        alVueVue.addConditions(alVueVueConditionBack);
        assertThat(alVueVue.getConditions()).containsOnly(alVueVueConditionBack);
        assertThat(alVueVueConditionBack.getParent()).isEqualTo(alVueVue);

        alVueVue.removeConditions(alVueVueConditionBack);
        assertThat(alVueVue.getConditions()).doesNotContain(alVueVueConditionBack);
        assertThat(alVueVueConditionBack.getParent()).isNull();

        alVueVue.conditions(new HashSet<>(Set.of(alVueVueConditionBack)));
        assertThat(alVueVue.getConditions()).containsOnly(alVueVueConditionBack);
        assertThat(alVueVueConditionBack.getParent()).isEqualTo(alVueVue);

        alVueVue.setConditions(new HashSet<>());
        assertThat(alVueVue.getConditions()).doesNotContain(alVueVueConditionBack);
        assertThat(alVueVueConditionBack.getParent()).isNull();
    }
}
