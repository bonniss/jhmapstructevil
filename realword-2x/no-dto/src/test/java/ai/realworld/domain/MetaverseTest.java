package ai.realworld.domain;

import static ai.realworld.domain.AlProProTestSamples.*;
import static ai.realworld.domain.AlProProViTestSamples.*;
import static ai.realworld.domain.AlProtyTestSamples.*;
import static ai.realworld.domain.AlProtyViTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MetaverseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metaverse.class);
        Metaverse metaverse1 = getMetaverseSample1();
        Metaverse metaverse2 = new Metaverse();
        assertThat(metaverse1).isNotEqualTo(metaverse2);

        metaverse2.setId(metaverse1.getId());
        assertThat(metaverse1).isEqualTo(metaverse2);

        metaverse2 = getMetaverseSample2();
        assertThat(metaverse1).isNotEqualTo(metaverse2);
    }

    @Test
    void alProProTest() {
        Metaverse metaverse = getMetaverseRandomSampleGenerator();
        AlProPro alProProBack = getAlProProRandomSampleGenerator();

        metaverse.addAlProPro(alProProBack);
        assertThat(metaverse.getAlProPros()).containsOnly(alProProBack);
        assertThat(alProProBack.getImages()).containsOnly(metaverse);

        metaverse.removeAlProPro(alProProBack);
        assertThat(metaverse.getAlProPros()).doesNotContain(alProProBack);
        assertThat(alProProBack.getImages()).doesNotContain(metaverse);

        metaverse.alProPros(new HashSet<>(Set.of(alProProBack)));
        assertThat(metaverse.getAlProPros()).containsOnly(alProProBack);
        assertThat(alProProBack.getImages()).containsOnly(metaverse);

        metaverse.setAlProPros(new HashSet<>());
        assertThat(metaverse.getAlProPros()).doesNotContain(alProProBack);
        assertThat(alProProBack.getImages()).doesNotContain(metaverse);
    }

    @Test
    void alProtyTest() {
        Metaverse metaverse = getMetaverseRandomSampleGenerator();
        AlProty alProtyBack = getAlProtyRandomSampleGenerator();

        metaverse.addAlProty(alProtyBack);
        assertThat(metaverse.getAlProties()).containsOnly(alProtyBack);
        assertThat(alProtyBack.getImages()).containsOnly(metaverse);

        metaverse.removeAlProty(alProtyBack);
        assertThat(metaverse.getAlProties()).doesNotContain(alProtyBack);
        assertThat(alProtyBack.getImages()).doesNotContain(metaverse);

        metaverse.alProties(new HashSet<>(Set.of(alProtyBack)));
        assertThat(metaverse.getAlProties()).containsOnly(alProtyBack);
        assertThat(alProtyBack.getImages()).containsOnly(metaverse);

        metaverse.setAlProties(new HashSet<>());
        assertThat(metaverse.getAlProties()).doesNotContain(alProtyBack);
        assertThat(alProtyBack.getImages()).doesNotContain(metaverse);
    }

    @Test
    void alProProViTest() {
        Metaverse metaverse = getMetaverseRandomSampleGenerator();
        AlProProVi alProProViBack = getAlProProViRandomSampleGenerator();

        metaverse.addAlProProVi(alProProViBack);
        assertThat(metaverse.getAlProProVis()).containsOnly(alProProViBack);
        assertThat(alProProViBack.getImages()).containsOnly(metaverse);

        metaverse.removeAlProProVi(alProProViBack);
        assertThat(metaverse.getAlProProVis()).doesNotContain(alProProViBack);
        assertThat(alProProViBack.getImages()).doesNotContain(metaverse);

        metaverse.alProProVis(new HashSet<>(Set.of(alProProViBack)));
        assertThat(metaverse.getAlProProVis()).containsOnly(alProProViBack);
        assertThat(alProProViBack.getImages()).containsOnly(metaverse);

        metaverse.setAlProProVis(new HashSet<>());
        assertThat(metaverse.getAlProProVis()).doesNotContain(alProProViBack);
        assertThat(alProProViBack.getImages()).doesNotContain(metaverse);
    }

    @Test
    void alProtyViTest() {
        Metaverse metaverse = getMetaverseRandomSampleGenerator();
        AlProtyVi alProtyViBack = getAlProtyViRandomSampleGenerator();

        metaverse.addAlProtyVi(alProtyViBack);
        assertThat(metaverse.getAlProtyVis()).containsOnly(alProtyViBack);
        assertThat(alProtyViBack.getImages()).containsOnly(metaverse);

        metaverse.removeAlProtyVi(alProtyViBack);
        assertThat(metaverse.getAlProtyVis()).doesNotContain(alProtyViBack);
        assertThat(alProtyViBack.getImages()).doesNotContain(metaverse);

        metaverse.alProtyVis(new HashSet<>(Set.of(alProtyViBack)));
        assertThat(metaverse.getAlProtyVis()).containsOnly(alProtyViBack);
        assertThat(alProtyViBack.getImages()).containsOnly(metaverse);

        metaverse.setAlProtyVis(new HashSet<>());
        assertThat(metaverse.getAlProtyVis()).doesNotContain(alProtyViBack);
        assertThat(alProtyViBack.getImages()).doesNotContain(metaverse);
    }
}
