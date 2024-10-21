package ai.realworld.domain;

import static ai.realworld.domain.AlSherMaleTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlSherMaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlSherMale.class);
        AlSherMale alSherMale1 = getAlSherMaleSample1();
        AlSherMale alSherMale2 = new AlSherMale();
        assertThat(alSherMale1).isNotEqualTo(alSherMale2);

        alSherMale2.setId(alSherMale1.getId());
        assertThat(alSherMale1).isEqualTo(alSherMale2);

        alSherMale2 = getAlSherMaleSample2();
        assertThat(alSherMale1).isNotEqualTo(alSherMale2);
    }

    @Test
    void applicationTest() {
        AlSherMale alSherMale = getAlSherMaleRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alSherMale.setApplication(johnLennonBack);
        assertThat(alSherMale.getApplication()).isEqualTo(johnLennonBack);

        alSherMale.application(null);
        assertThat(alSherMale.getApplication()).isNull();
    }
}
