package ai.realworld.domain;

import static ai.realworld.domain.AlSherMaleViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlSherMaleViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlSherMaleVi.class);
        AlSherMaleVi alSherMaleVi1 = getAlSherMaleViSample1();
        AlSherMaleVi alSherMaleVi2 = new AlSherMaleVi();
        assertThat(alSherMaleVi1).isNotEqualTo(alSherMaleVi2);

        alSherMaleVi2.setId(alSherMaleVi1.getId());
        assertThat(alSherMaleVi1).isEqualTo(alSherMaleVi2);

        alSherMaleVi2 = getAlSherMaleViSample2();
        assertThat(alSherMaleVi1).isNotEqualTo(alSherMaleVi2);
    }

    @Test
    void applicationTest() {
        AlSherMaleVi alSherMaleVi = getAlSherMaleViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alSherMaleVi.setApplication(johnLennonBack);
        assertThat(alSherMaleVi.getApplication()).isEqualTo(johnLennonBack);

        alSherMaleVi.application(null);
        assertThat(alSherMaleVi.getApplication()).isNull();
    }
}
