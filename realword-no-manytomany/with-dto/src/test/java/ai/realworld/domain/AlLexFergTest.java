package ai.realworld.domain;

import static ai.realworld.domain.AlCatalinaTestSamples.*;
import static ai.realworld.domain.AlLexFergTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlLexFergTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLexFerg.class);
        AlLexFerg alLexFerg1 = getAlLexFergSample1();
        AlLexFerg alLexFerg2 = new AlLexFerg();
        assertThat(alLexFerg1).isNotEqualTo(alLexFerg2);

        alLexFerg2.setId(alLexFerg1.getId());
        assertThat(alLexFerg1).isEqualTo(alLexFerg2);

        alLexFerg2 = getAlLexFergSample2();
        assertThat(alLexFerg1).isNotEqualTo(alLexFerg2);
    }

    @Test
    void avatarTest() {
        AlLexFerg alLexFerg = getAlLexFergRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alLexFerg.setAvatar(metaverseBack);
        assertThat(alLexFerg.getAvatar()).isEqualTo(metaverseBack);

        alLexFerg.avatar(null);
        assertThat(alLexFerg.getAvatar()).isNull();
    }

    @Test
    void categoryTest() {
        AlLexFerg alLexFerg = getAlLexFergRandomSampleGenerator();
        AlCatalina alCatalinaBack = getAlCatalinaRandomSampleGenerator();

        alLexFerg.setCategory(alCatalinaBack);
        assertThat(alLexFerg.getCategory()).isEqualTo(alCatalinaBack);

        alLexFerg.category(null);
        assertThat(alLexFerg.getCategory()).isNull();
    }

    @Test
    void applicationTest() {
        AlLexFerg alLexFerg = getAlLexFergRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alLexFerg.setApplication(johnLennonBack);
        assertThat(alLexFerg.getApplication()).isEqualTo(johnLennonBack);

        alLexFerg.application(null);
        assertThat(alLexFerg.getApplication()).isNull();
    }
}
