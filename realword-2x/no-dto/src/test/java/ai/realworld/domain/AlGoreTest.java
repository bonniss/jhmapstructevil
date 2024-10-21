package ai.realworld.domain;

import static ai.realworld.domain.AlBetonamuRelationTestSamples.*;
import static ai.realworld.domain.AlBetonamuRelationViTestSamples.*;
import static ai.realworld.domain.AlGoreConditionTestSamples.*;
import static ai.realworld.domain.AlGoreConditionViTestSamples.*;
import static ai.realworld.domain.AlGoreTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlGoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGore.class);
        AlGore alGore1 = getAlGoreSample1();
        AlGore alGore2 = new AlGore();
        assertThat(alGore1).isNotEqualTo(alGore2);

        alGore2.setId(alGore1.getId());
        assertThat(alGore1).isEqualTo(alGore2);

        alGore2 = getAlGoreSample2();
        assertThat(alGore1).isNotEqualTo(alGore2);
    }

    @Test
    void bizRelationTest() {
        AlGore alGore = getAlGoreRandomSampleGenerator();
        AlBetonamuRelation alBetonamuRelationBack = getAlBetonamuRelationRandomSampleGenerator();

        alGore.setBizRelation(alBetonamuRelationBack);
        assertThat(alGore.getBizRelation()).isEqualTo(alBetonamuRelationBack);

        alGore.bizRelation(null);
        assertThat(alGore.getBizRelation()).isNull();
    }

    @Test
    void applicationTest() {
        AlGore alGore = getAlGoreRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alGore.setApplication(johnLennonBack);
        assertThat(alGore.getApplication()).isEqualTo(johnLennonBack);

        alGore.application(null);
        assertThat(alGore.getApplication()).isNull();
    }

    @Test
    void bizRelationViTest() {
        AlGore alGore = getAlGoreRandomSampleGenerator();
        AlBetonamuRelationVi alBetonamuRelationViBack = getAlBetonamuRelationViRandomSampleGenerator();

        alGore.setBizRelationVi(alBetonamuRelationViBack);
        assertThat(alGore.getBizRelationVi()).isEqualTo(alBetonamuRelationViBack);

        alGore.bizRelationVi(null);
        assertThat(alGore.getBizRelationVi()).isNull();
    }

    @Test
    void conditionsTest() {
        AlGore alGore = getAlGoreRandomSampleGenerator();
        AlGoreCondition alGoreConditionBack = getAlGoreConditionRandomSampleGenerator();

        alGore.addConditions(alGoreConditionBack);
        assertThat(alGore.getConditions()).containsOnly(alGoreConditionBack);
        assertThat(alGoreConditionBack.getParent()).isEqualTo(alGore);

        alGore.removeConditions(alGoreConditionBack);
        assertThat(alGore.getConditions()).doesNotContain(alGoreConditionBack);
        assertThat(alGoreConditionBack.getParent()).isNull();

        alGore.conditions(new HashSet<>(Set.of(alGoreConditionBack)));
        assertThat(alGore.getConditions()).containsOnly(alGoreConditionBack);
        assertThat(alGoreConditionBack.getParent()).isEqualTo(alGore);

        alGore.setConditions(new HashSet<>());
        assertThat(alGore.getConditions()).doesNotContain(alGoreConditionBack);
        assertThat(alGoreConditionBack.getParent()).isNull();
    }

    @Test
    void conditionVisTest() {
        AlGore alGore = getAlGoreRandomSampleGenerator();
        AlGoreConditionVi alGoreConditionViBack = getAlGoreConditionViRandomSampleGenerator();

        alGore.addConditionVis(alGoreConditionViBack);
        assertThat(alGore.getConditionVis()).containsOnly(alGoreConditionViBack);
        assertThat(alGoreConditionViBack.getParent()).isEqualTo(alGore);

        alGore.removeConditionVis(alGoreConditionViBack);
        assertThat(alGore.getConditionVis()).doesNotContain(alGoreConditionViBack);
        assertThat(alGoreConditionViBack.getParent()).isNull();

        alGore.conditionVis(new HashSet<>(Set.of(alGoreConditionViBack)));
        assertThat(alGore.getConditionVis()).containsOnly(alGoreConditionViBack);
        assertThat(alGoreConditionViBack.getParent()).isEqualTo(alGore);

        alGore.setConditionVis(new HashSet<>());
        assertThat(alGore.getConditionVis()).doesNotContain(alGoreConditionViBack);
        assertThat(alGoreConditionViBack.getParent()).isNull();
    }
}
