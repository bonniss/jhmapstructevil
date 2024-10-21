package ai.realworld.domain;

import static ai.realworld.domain.AlDesireTestSamples.*;
import static ai.realworld.domain.AlLeandroTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlLeandroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLeandro.class);
        AlLeandro alLeandro1 = getAlLeandroSample1();
        AlLeandro alLeandro2 = new AlLeandro();
        assertThat(alLeandro1).isNotEqualTo(alLeandro2);

        alLeandro2.setId(alLeandro1.getId());
        assertThat(alLeandro1).isEqualTo(alLeandro2);

        alLeandro2 = getAlLeandroSample2();
        assertThat(alLeandro1).isNotEqualTo(alLeandro2);
    }

    @Test
    void programBackgroundTest() {
        AlLeandro alLeandro = getAlLeandroRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alLeandro.setProgramBackground(metaverseBack);
        assertThat(alLeandro.getProgramBackground()).isEqualTo(metaverseBack);

        alLeandro.programBackground(null);
        assertThat(alLeandro.getProgramBackground()).isNull();
    }

    @Test
    void wheelBackgroundTest() {
        AlLeandro alLeandro = getAlLeandroRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alLeandro.setWheelBackground(metaverseBack);
        assertThat(alLeandro.getWheelBackground()).isEqualTo(metaverseBack);

        alLeandro.wheelBackground(null);
        assertThat(alLeandro.getWheelBackground()).isNull();
    }

    @Test
    void applicationTest() {
        AlLeandro alLeandro = getAlLeandroRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alLeandro.setApplication(johnLennonBack);
        assertThat(alLeandro.getApplication()).isEqualTo(johnLennonBack);

        alLeandro.application(null);
        assertThat(alLeandro.getApplication()).isNull();
    }

    @Test
    void awardsTest() {
        AlLeandro alLeandro = getAlLeandroRandomSampleGenerator();
        AlDesire alDesireBack = getAlDesireRandomSampleGenerator();

        alLeandro.addAwards(alDesireBack);
        assertThat(alLeandro.getAwards()).containsOnly(alDesireBack);
        assertThat(alDesireBack.getMiniGame()).isEqualTo(alLeandro);

        alLeandro.removeAwards(alDesireBack);
        assertThat(alLeandro.getAwards()).doesNotContain(alDesireBack);
        assertThat(alDesireBack.getMiniGame()).isNull();

        alLeandro.awards(new HashSet<>(Set.of(alDesireBack)));
        assertThat(alLeandro.getAwards()).containsOnly(alDesireBack);
        assertThat(alDesireBack.getMiniGame()).isEqualTo(alLeandro);

        alLeandro.setAwards(new HashSet<>());
        assertThat(alLeandro.getAwards()).doesNotContain(alDesireBack);
        assertThat(alDesireBack.getMiniGame()).isNull();
    }
}
