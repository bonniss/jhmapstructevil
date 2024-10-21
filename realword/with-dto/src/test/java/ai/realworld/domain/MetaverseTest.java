package ai.realworld.domain;

import static ai.realworld.domain.AlProProTestSamples.*;
import static ai.realworld.domain.AlProtyTestSamples.*;
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
}
