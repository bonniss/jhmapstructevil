package ai.realworld.domain;

import static ai.realworld.domain.AlDesireTestSamples.*;
import static ai.realworld.domain.AlLeandroPlayingTimeTestSamples.*;
import static ai.realworld.domain.AlLeandroTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlLeandroPlayingTimeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLeandroPlayingTime.class);
        AlLeandroPlayingTime alLeandroPlayingTime1 = getAlLeandroPlayingTimeSample1();
        AlLeandroPlayingTime alLeandroPlayingTime2 = new AlLeandroPlayingTime();
        assertThat(alLeandroPlayingTime1).isNotEqualTo(alLeandroPlayingTime2);

        alLeandroPlayingTime2.setId(alLeandroPlayingTime1.getId());
        assertThat(alLeandroPlayingTime1).isEqualTo(alLeandroPlayingTime2);

        alLeandroPlayingTime2 = getAlLeandroPlayingTimeSample2();
        assertThat(alLeandroPlayingTime1).isNotEqualTo(alLeandroPlayingTime2);
    }

    @Test
    void maggiTest() {
        AlLeandroPlayingTime alLeandroPlayingTime = getAlLeandroPlayingTimeRandomSampleGenerator();
        AlLeandro alLeandroBack = getAlLeandroRandomSampleGenerator();

        alLeandroPlayingTime.setMaggi(alLeandroBack);
        assertThat(alLeandroPlayingTime.getMaggi()).isEqualTo(alLeandroBack);

        alLeandroPlayingTime.maggi(null);
        assertThat(alLeandroPlayingTime.getMaggi()).isNull();
    }

    @Test
    void userTest() {
        AlLeandroPlayingTime alLeandroPlayingTime = getAlLeandroPlayingTimeRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alLeandroPlayingTime.setUser(alPacinoBack);
        assertThat(alLeandroPlayingTime.getUser()).isEqualTo(alPacinoBack);

        alLeandroPlayingTime.user(null);
        assertThat(alLeandroPlayingTime.getUser()).isNull();
    }

    @Test
    void awardTest() {
        AlLeandroPlayingTime alLeandroPlayingTime = getAlLeandroPlayingTimeRandomSampleGenerator();
        AlDesire alDesireBack = getAlDesireRandomSampleGenerator();

        alLeandroPlayingTime.setAward(alDesireBack);
        assertThat(alLeandroPlayingTime.getAward()).isEqualTo(alDesireBack);

        alLeandroPlayingTime.award(null);
        assertThat(alLeandroPlayingTime.getAward()).isNull();
    }

    @Test
    void applicationTest() {
        AlLeandroPlayingTime alLeandroPlayingTime = getAlLeandroPlayingTimeRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alLeandroPlayingTime.setApplication(johnLennonBack);
        assertThat(alLeandroPlayingTime.getApplication()).isEqualTo(johnLennonBack);

        alLeandroPlayingTime.application(null);
        assertThat(alLeandroPlayingTime.getApplication()).isNull();
    }
}
