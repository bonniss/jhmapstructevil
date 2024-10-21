package ai.realworld.domain;

import static ai.realworld.domain.AlCatalinaViTestSamples.*;
import static ai.realworld.domain.AlCatalinaViTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlCatalinaViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlCatalinaVi.class);
        AlCatalinaVi alCatalinaVi1 = getAlCatalinaViSample1();
        AlCatalinaVi alCatalinaVi2 = new AlCatalinaVi();
        assertThat(alCatalinaVi1).isNotEqualTo(alCatalinaVi2);

        alCatalinaVi2.setId(alCatalinaVi1.getId());
        assertThat(alCatalinaVi1).isEqualTo(alCatalinaVi2);

        alCatalinaVi2 = getAlCatalinaViSample2();
        assertThat(alCatalinaVi1).isNotEqualTo(alCatalinaVi2);
    }

    @Test
    void parentTest() {
        AlCatalinaVi alCatalinaVi = getAlCatalinaViRandomSampleGenerator();
        AlCatalinaVi alCatalinaViBack = getAlCatalinaViRandomSampleGenerator();

        alCatalinaVi.setParent(alCatalinaViBack);
        assertThat(alCatalinaVi.getParent()).isEqualTo(alCatalinaViBack);

        alCatalinaVi.parent(null);
        assertThat(alCatalinaVi.getParent()).isNull();
    }

    @Test
    void avatarTest() {
        AlCatalinaVi alCatalinaVi = getAlCatalinaViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alCatalinaVi.setAvatar(metaverseBack);
        assertThat(alCatalinaVi.getAvatar()).isEqualTo(metaverseBack);

        alCatalinaVi.avatar(null);
        assertThat(alCatalinaVi.getAvatar()).isNull();
    }

    @Test
    void childrenTest() {
        AlCatalinaVi alCatalinaVi = getAlCatalinaViRandomSampleGenerator();
        AlCatalinaVi alCatalinaViBack = getAlCatalinaViRandomSampleGenerator();

        alCatalinaVi.addChildren(alCatalinaViBack);
        assertThat(alCatalinaVi.getChildren()).containsOnly(alCatalinaViBack);
        assertThat(alCatalinaViBack.getParent()).isEqualTo(alCatalinaVi);

        alCatalinaVi.removeChildren(alCatalinaViBack);
        assertThat(alCatalinaVi.getChildren()).doesNotContain(alCatalinaViBack);
        assertThat(alCatalinaViBack.getParent()).isNull();

        alCatalinaVi.children(new HashSet<>(Set.of(alCatalinaViBack)));
        assertThat(alCatalinaVi.getChildren()).containsOnly(alCatalinaViBack);
        assertThat(alCatalinaViBack.getParent()).isEqualTo(alCatalinaVi);

        alCatalinaVi.setChildren(new HashSet<>());
        assertThat(alCatalinaVi.getChildren()).doesNotContain(alCatalinaViBack);
        assertThat(alCatalinaViBack.getParent()).isNull();
    }
}
