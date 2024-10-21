package ai.realworld.domain;

import static ai.realworld.domain.AntonioBanderasTestSamples.*;
import static ai.realworld.domain.AntonioBanderasTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AntonioBanderasTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AntonioBanderas.class);
        AntonioBanderas antonioBanderas1 = getAntonioBanderasSample1();
        AntonioBanderas antonioBanderas2 = new AntonioBanderas();
        assertThat(antonioBanderas1).isNotEqualTo(antonioBanderas2);

        antonioBanderas2.setId(antonioBanderas1.getId());
        assertThat(antonioBanderas1).isEqualTo(antonioBanderas2);

        antonioBanderas2 = getAntonioBanderasSample2();
        assertThat(antonioBanderas1).isNotEqualTo(antonioBanderas2);
    }

    @Test
    void currentTest() {
        AntonioBanderas antonioBanderas = getAntonioBanderasRandomSampleGenerator();
        AntonioBanderas antonioBanderasBack = getAntonioBanderasRandomSampleGenerator();

        antonioBanderas.setCurrent(antonioBanderasBack);
        assertThat(antonioBanderas.getCurrent()).isEqualTo(antonioBanderasBack);

        antonioBanderas.current(null);
        assertThat(antonioBanderas.getCurrent()).isNull();
    }

    @Test
    void parentTest() {
        AntonioBanderas antonioBanderas = getAntonioBanderasRandomSampleGenerator();
        AntonioBanderas antonioBanderasBack = getAntonioBanderasRandomSampleGenerator();

        antonioBanderas.setParent(antonioBanderasBack);
        assertThat(antonioBanderas.getParent()).isEqualTo(antonioBanderasBack);

        antonioBanderas.parent(null);
        assertThat(antonioBanderas.getParent()).isNull();
    }

    @Test
    void childrenTest() {
        AntonioBanderas antonioBanderas = getAntonioBanderasRandomSampleGenerator();
        AntonioBanderas antonioBanderasBack = getAntonioBanderasRandomSampleGenerator();

        antonioBanderas.addChildren(antonioBanderasBack);
        assertThat(antonioBanderas.getChildren()).containsOnly(antonioBanderasBack);
        assertThat(antonioBanderasBack.getParent()).isEqualTo(antonioBanderas);

        antonioBanderas.removeChildren(antonioBanderasBack);
        assertThat(antonioBanderas.getChildren()).doesNotContain(antonioBanderasBack);
        assertThat(antonioBanderasBack.getParent()).isNull();

        antonioBanderas.children(new HashSet<>(Set.of(antonioBanderasBack)));
        assertThat(antonioBanderas.getChildren()).containsOnly(antonioBanderasBack);
        assertThat(antonioBanderasBack.getParent()).isEqualTo(antonioBanderas);

        antonioBanderas.setChildren(new HashSet<>());
        assertThat(antonioBanderas.getChildren()).doesNotContain(antonioBanderasBack);
        assertThat(antonioBanderasBack.getParent()).isNull();
    }

    @Test
    void antonioBanderasTest() {
        AntonioBanderas antonioBanderas = getAntonioBanderasRandomSampleGenerator();
        AntonioBanderas antonioBanderasBack = getAntonioBanderasRandomSampleGenerator();

        antonioBanderas.setAntonioBanderas(antonioBanderasBack);
        assertThat(antonioBanderas.getAntonioBanderas()).isEqualTo(antonioBanderasBack);
        assertThat(antonioBanderasBack.getCurrent()).isEqualTo(antonioBanderas);

        antonioBanderas.antonioBanderas(null);
        assertThat(antonioBanderas.getAntonioBanderas()).isNull();
        assertThat(antonioBanderasBack.getCurrent()).isNull();
    }
}
