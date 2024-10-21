package ai.realworld.domain;

import static ai.realworld.domain.AlMenityViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlMenityViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMenityVi.class);
        AlMenityVi alMenityVi1 = getAlMenityViSample1();
        AlMenityVi alMenityVi2 = new AlMenityVi();
        assertThat(alMenityVi1).isNotEqualTo(alMenityVi2);

        alMenityVi2.setId(alMenityVi1.getId());
        assertThat(alMenityVi1).isEqualTo(alMenityVi2);

        alMenityVi2 = getAlMenityViSample2();
        assertThat(alMenityVi1).isNotEqualTo(alMenityVi2);
    }

    @Test
    void applicationTest() {
        AlMenityVi alMenityVi = getAlMenityViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alMenityVi.setApplication(johnLennonBack);
        assertThat(alMenityVi.getApplication()).isEqualTo(johnLennonBack);

        alMenityVi.application(null);
        assertThat(alMenityVi.getApplication()).isNull();
    }
}
