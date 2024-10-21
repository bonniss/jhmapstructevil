package ai.realworld.domain;

import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
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
}
