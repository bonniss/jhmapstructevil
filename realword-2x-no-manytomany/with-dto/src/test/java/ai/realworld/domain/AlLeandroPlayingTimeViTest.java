package ai.realworld.domain;

import static ai.realworld.domain.AlDesireTestSamples.*;
import static ai.realworld.domain.AlLeandroPlayingTimeViTestSamples.*;
import static ai.realworld.domain.AlLeandroTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlLeandroPlayingTimeViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLeandroPlayingTimeVi.class);
        AlLeandroPlayingTimeVi alLeandroPlayingTimeVi1 = getAlLeandroPlayingTimeViSample1();
        AlLeandroPlayingTimeVi alLeandroPlayingTimeVi2 = new AlLeandroPlayingTimeVi();
        assertThat(alLeandroPlayingTimeVi1).isNotEqualTo(alLeandroPlayingTimeVi2);

        alLeandroPlayingTimeVi2.setId(alLeandroPlayingTimeVi1.getId());
        assertThat(alLeandroPlayingTimeVi1).isEqualTo(alLeandroPlayingTimeVi2);

        alLeandroPlayingTimeVi2 = getAlLeandroPlayingTimeViSample2();
        assertThat(alLeandroPlayingTimeVi1).isNotEqualTo(alLeandroPlayingTimeVi2);
    }

    @Test
    void maggiTest() {
        AlLeandroPlayingTimeVi alLeandroPlayingTimeVi = getAlLeandroPlayingTimeViRandomSampleGenerator();
        AlLeandro alLeandroBack = getAlLeandroRandomSampleGenerator();

        alLeandroPlayingTimeVi.setMaggi(alLeandroBack);
        assertThat(alLeandroPlayingTimeVi.getMaggi()).isEqualTo(alLeandroBack);

        alLeandroPlayingTimeVi.maggi(null);
        assertThat(alLeandroPlayingTimeVi.getMaggi()).isNull();
    }

    @Test
    void userTest() {
        AlLeandroPlayingTimeVi alLeandroPlayingTimeVi = getAlLeandroPlayingTimeViRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alLeandroPlayingTimeVi.setUser(alPacinoBack);
        assertThat(alLeandroPlayingTimeVi.getUser()).isEqualTo(alPacinoBack);

        alLeandroPlayingTimeVi.user(null);
        assertThat(alLeandroPlayingTimeVi.getUser()).isNull();
    }

    @Test
    void awardTest() {
        AlLeandroPlayingTimeVi alLeandroPlayingTimeVi = getAlLeandroPlayingTimeViRandomSampleGenerator();
        AlDesire alDesireBack = getAlDesireRandomSampleGenerator();

        alLeandroPlayingTimeVi.setAward(alDesireBack);
        assertThat(alLeandroPlayingTimeVi.getAward()).isEqualTo(alDesireBack);

        alLeandroPlayingTimeVi.award(null);
        assertThat(alLeandroPlayingTimeVi.getAward()).isNull();
    }

    @Test
    void applicationTest() {
        AlLeandroPlayingTimeVi alLeandroPlayingTimeVi = getAlLeandroPlayingTimeViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alLeandroPlayingTimeVi.setApplication(johnLennonBack);
        assertThat(alLeandroPlayingTimeVi.getApplication()).isEqualTo(johnLennonBack);

        alLeandroPlayingTimeVi.application(null);
        assertThat(alLeandroPlayingTimeVi.getApplication()).isNull();
    }
}
