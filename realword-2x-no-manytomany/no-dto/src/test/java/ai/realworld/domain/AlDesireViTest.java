package ai.realworld.domain;

import static ai.realworld.domain.AlDesireViTestSamples.*;
import static ai.realworld.domain.AlLeandroTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlDesireViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlDesireVi.class);
        AlDesireVi alDesireVi1 = getAlDesireViSample1();
        AlDesireVi alDesireVi2 = new AlDesireVi();
        assertThat(alDesireVi1).isNotEqualTo(alDesireVi2);

        alDesireVi2.setId(alDesireVi1.getId());
        assertThat(alDesireVi1).isEqualTo(alDesireVi2);

        alDesireVi2 = getAlDesireViSample2();
        assertThat(alDesireVi1).isNotEqualTo(alDesireVi2);
    }

    @Test
    void imageTest() {
        AlDesireVi alDesireVi = getAlDesireViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alDesireVi.setImage(metaverseBack);
        assertThat(alDesireVi.getImage()).isEqualTo(metaverseBack);

        alDesireVi.image(null);
        assertThat(alDesireVi.getImage()).isNull();
    }

    @Test
    void maggiTest() {
        AlDesireVi alDesireVi = getAlDesireViRandomSampleGenerator();
        AlLeandro alLeandroBack = getAlLeandroRandomSampleGenerator();

        alDesireVi.setMaggi(alLeandroBack);
        assertThat(alDesireVi.getMaggi()).isEqualTo(alLeandroBack);

        alDesireVi.maggi(null);
        assertThat(alDesireVi.getMaggi()).isNull();
    }
}
