package ai.realworld.domain;

import static ai.realworld.domain.AlLexFergViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlLexFergViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLexFergVi.class);
        AlLexFergVi alLexFergVi1 = getAlLexFergViSample1();
        AlLexFergVi alLexFergVi2 = new AlLexFergVi();
        assertThat(alLexFergVi1).isNotEqualTo(alLexFergVi2);

        alLexFergVi2.setId(alLexFergVi1.getId());
        assertThat(alLexFergVi1).isEqualTo(alLexFergVi2);

        alLexFergVi2 = getAlLexFergViSample2();
        assertThat(alLexFergVi1).isNotEqualTo(alLexFergVi2);
    }
}
