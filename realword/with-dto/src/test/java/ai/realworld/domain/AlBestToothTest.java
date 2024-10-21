package ai.realworld.domain;

import static ai.realworld.domain.AlBestToothTestSamples.*;
import static ai.realworld.domain.AlLexFergTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlBestToothTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBestTooth.class);
        AlBestTooth alBestTooth1 = getAlBestToothSample1();
        AlBestTooth alBestTooth2 = new AlBestTooth();
        assertThat(alBestTooth1).isNotEqualTo(alBestTooth2);

        alBestTooth2.setId(alBestTooth1.getId());
        assertThat(alBestTooth1).isEqualTo(alBestTooth2);

        alBestTooth2 = getAlBestToothSample2();
        assertThat(alBestTooth1).isNotEqualTo(alBestTooth2);
    }

    @Test
    void applicationTest() {
        AlBestTooth alBestTooth = getAlBestToothRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alBestTooth.setApplication(johnLennonBack);
        assertThat(alBestTooth.getApplication()).isEqualTo(johnLennonBack);

        alBestTooth.application(null);
        assertThat(alBestTooth.getApplication()).isNull();
    }

    @Test
    void articleTest() {
        AlBestTooth alBestTooth = getAlBestToothRandomSampleGenerator();
        AlLexFerg alLexFergBack = getAlLexFergRandomSampleGenerator();

        alBestTooth.addArticle(alLexFergBack);
        assertThat(alBestTooth.getArticles()).containsOnly(alLexFergBack);
        assertThat(alLexFergBack.getTags()).containsOnly(alBestTooth);

        alBestTooth.removeArticle(alLexFergBack);
        assertThat(alBestTooth.getArticles()).doesNotContain(alLexFergBack);
        assertThat(alLexFergBack.getTags()).doesNotContain(alBestTooth);

        alBestTooth.articles(new HashSet<>(Set.of(alLexFergBack)));
        assertThat(alBestTooth.getArticles()).containsOnly(alLexFergBack);
        assertThat(alLexFergBack.getTags()).containsOnly(alBestTooth);

        alBestTooth.setArticles(new HashSet<>());
        assertThat(alBestTooth.getArticles()).doesNotContain(alLexFergBack);
        assertThat(alLexFergBack.getTags()).doesNotContain(alBestTooth);
    }
}
