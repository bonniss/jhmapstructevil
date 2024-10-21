package ai.realworld.domain;

import static ai.realworld.domain.SaisanCogViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaisanCogViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaisanCogVi.class);
        SaisanCogVi saisanCogVi1 = getSaisanCogViSample1();
        SaisanCogVi saisanCogVi2 = new SaisanCogVi();
        assertThat(saisanCogVi1).isNotEqualTo(saisanCogVi2);

        saisanCogVi2.setId(saisanCogVi1.getId());
        assertThat(saisanCogVi1).isEqualTo(saisanCogVi2);

        saisanCogVi2 = getSaisanCogViSample2();
        assertThat(saisanCogVi1).isNotEqualTo(saisanCogVi2);
    }
}
