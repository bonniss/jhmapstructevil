package ai.realworld.domain;

import static ai.realworld.domain.AntonioBanderasViTestSamples.*;
import static ai.realworld.domain.AntonioBanderasViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AntonioBanderasViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AntonioBanderasVi.class);
        AntonioBanderasVi antonioBanderasVi1 = getAntonioBanderasViSample1();
        AntonioBanderasVi antonioBanderasVi2 = new AntonioBanderasVi();
        assertThat(antonioBanderasVi1).isNotEqualTo(antonioBanderasVi2);

        antonioBanderasVi2.setId(antonioBanderasVi1.getId());
        assertThat(antonioBanderasVi1).isEqualTo(antonioBanderasVi2);

        antonioBanderasVi2 = getAntonioBanderasViSample2();
        assertThat(antonioBanderasVi1).isNotEqualTo(antonioBanderasVi2);
    }

    @Test
    void currentTest() {
        AntonioBanderasVi antonioBanderasVi = getAntonioBanderasViRandomSampleGenerator();
        AntonioBanderasVi antonioBanderasViBack = getAntonioBanderasViRandomSampleGenerator();

        antonioBanderasVi.setCurrent(antonioBanderasViBack);
        assertThat(antonioBanderasVi.getCurrent()).isEqualTo(antonioBanderasViBack);

        antonioBanderasVi.current(null);
        assertThat(antonioBanderasVi.getCurrent()).isNull();
    }

    @Test
    void parentTest() {
        AntonioBanderasVi antonioBanderasVi = getAntonioBanderasViRandomSampleGenerator();
        AntonioBanderasVi antonioBanderasViBack = getAntonioBanderasViRandomSampleGenerator();

        antonioBanderasVi.setParent(antonioBanderasViBack);
        assertThat(antonioBanderasVi.getParent()).isEqualTo(antonioBanderasViBack);

        antonioBanderasVi.parent(null);
        assertThat(antonioBanderasVi.getParent()).isNull();
    }

    @Test
    void childrenTest() {
        AntonioBanderasVi antonioBanderasVi = getAntonioBanderasViRandomSampleGenerator();
        AntonioBanderasVi antonioBanderasViBack = getAntonioBanderasViRandomSampleGenerator();

        antonioBanderasVi.addChildren(antonioBanderasViBack);
        assertThat(antonioBanderasVi.getChildren()).containsOnly(antonioBanderasViBack);
        assertThat(antonioBanderasViBack.getParent()).isEqualTo(antonioBanderasVi);

        antonioBanderasVi.removeChildren(antonioBanderasViBack);
        assertThat(antonioBanderasVi.getChildren()).doesNotContain(antonioBanderasViBack);
        assertThat(antonioBanderasViBack.getParent()).isNull();

        antonioBanderasVi.children(new HashSet<>(Set.of(antonioBanderasViBack)));
        assertThat(antonioBanderasVi.getChildren()).containsOnly(antonioBanderasViBack);
        assertThat(antonioBanderasViBack.getParent()).isEqualTo(antonioBanderasVi);

        antonioBanderasVi.setChildren(new HashSet<>());
        assertThat(antonioBanderasVi.getChildren()).doesNotContain(antonioBanderasViBack);
        assertThat(antonioBanderasViBack.getParent()).isNull();
    }

    @Test
    void antonioBanderasViTest() {
        AntonioBanderasVi antonioBanderasVi = getAntonioBanderasViRandomSampleGenerator();
        AntonioBanderasVi antonioBanderasViBack = getAntonioBanderasViRandomSampleGenerator();

        antonioBanderasVi.setAntonioBanderasVi(antonioBanderasViBack);
        assertThat(antonioBanderasVi.getAntonioBanderasVi()).isEqualTo(antonioBanderasViBack);
        assertThat(antonioBanderasViBack.getCurrent()).isEqualTo(antonioBanderasVi);

        antonioBanderasVi.antonioBanderasVi(null);
        assertThat(antonioBanderasVi.getAntonioBanderasVi()).isNull();
        assertThat(antonioBanderasViBack.getCurrent()).isNull();
    }
}
