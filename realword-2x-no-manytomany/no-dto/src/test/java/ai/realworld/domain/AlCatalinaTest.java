package ai.realworld.domain;

import static ai.realworld.domain.AlCatalinaTestSamples.*;
import static ai.realworld.domain.AlCatalinaTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlCatalinaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlCatalina.class);
        AlCatalina alCatalina1 = getAlCatalinaSample1();
        AlCatalina alCatalina2 = new AlCatalina();
        assertThat(alCatalina1).isNotEqualTo(alCatalina2);

        alCatalina2.setId(alCatalina1.getId());
        assertThat(alCatalina1).isEqualTo(alCatalina2);

        alCatalina2 = getAlCatalinaSample2();
        assertThat(alCatalina1).isNotEqualTo(alCatalina2);
    }

    @Test
    void parentTest() {
        AlCatalina alCatalina = getAlCatalinaRandomSampleGenerator();
        AlCatalina alCatalinaBack = getAlCatalinaRandomSampleGenerator();

        alCatalina.setParent(alCatalinaBack);
        assertThat(alCatalina.getParent()).isEqualTo(alCatalinaBack);

        alCatalina.parent(null);
        assertThat(alCatalina.getParent()).isNull();
    }

    @Test
    void avatarTest() {
        AlCatalina alCatalina = getAlCatalinaRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alCatalina.setAvatar(metaverseBack);
        assertThat(alCatalina.getAvatar()).isEqualTo(metaverseBack);

        alCatalina.avatar(null);
        assertThat(alCatalina.getAvatar()).isNull();
    }

    @Test
    void applicationTest() {
        AlCatalina alCatalina = getAlCatalinaRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alCatalina.setApplication(johnLennonBack);
        assertThat(alCatalina.getApplication()).isEqualTo(johnLennonBack);

        alCatalina.application(null);
        assertThat(alCatalina.getApplication()).isNull();
    }

    @Test
    void childrenTest() {
        AlCatalina alCatalina = getAlCatalinaRandomSampleGenerator();
        AlCatalina alCatalinaBack = getAlCatalinaRandomSampleGenerator();

        alCatalina.addChildren(alCatalinaBack);
        assertThat(alCatalina.getChildren()).containsOnly(alCatalinaBack);
        assertThat(alCatalinaBack.getParent()).isEqualTo(alCatalina);

        alCatalina.removeChildren(alCatalinaBack);
        assertThat(alCatalina.getChildren()).doesNotContain(alCatalinaBack);
        assertThat(alCatalinaBack.getParent()).isNull();

        alCatalina.children(new HashSet<>(Set.of(alCatalinaBack)));
        assertThat(alCatalina.getChildren()).containsOnly(alCatalinaBack);
        assertThat(alCatalinaBack.getParent()).isEqualTo(alCatalina);

        alCatalina.setChildren(new HashSet<>());
        assertThat(alCatalina.getChildren()).doesNotContain(alCatalinaBack);
        assertThat(alCatalinaBack.getParent()).isNull();
    }
}
