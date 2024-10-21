package ai.realworld.domain;

import static ai.realworld.domain.AlDesireTestSamples.*;
import static ai.realworld.domain.AlLeandroTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlDesireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlDesire.class);
        AlDesire alDesire1 = getAlDesireSample1();
        AlDesire alDesire2 = new AlDesire();
        assertThat(alDesire1).isNotEqualTo(alDesire2);

        alDesire2.setId(alDesire1.getId());
        assertThat(alDesire1).isEqualTo(alDesire2);

        alDesire2 = getAlDesireSample2();
        assertThat(alDesire1).isNotEqualTo(alDesire2);
    }

    @Test
    void imageTest() {
        AlDesire alDesire = getAlDesireRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alDesire.setImage(metaverseBack);
        assertThat(alDesire.getImage()).isEqualTo(metaverseBack);

        alDesire.image(null);
        assertThat(alDesire.getImage()).isNull();
    }

    @Test
    void miniGameTest() {
        AlDesire alDesire = getAlDesireRandomSampleGenerator();
        AlLeandro alLeandroBack = getAlLeandroRandomSampleGenerator();

        alDesire.setMiniGame(alLeandroBack);
        assertThat(alDesire.getMiniGame()).isEqualTo(alLeandroBack);

        alDesire.miniGame(null);
        assertThat(alDesire.getMiniGame()).isNull();
    }

    @Test
    void applicationTest() {
        AlDesire alDesire = getAlDesireRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alDesire.setApplication(johnLennonBack);
        assertThat(alDesire.getApplication()).isEqualTo(johnLennonBack);

        alDesire.application(null);
        assertThat(alDesire.getApplication()).isNull();
    }
}
