package ai.realworld.domain;

import static ai.realworld.domain.SaisanCogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaisanCogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaisanCog.class);
        SaisanCog saisanCog1 = getSaisanCogSample1();
        SaisanCog saisanCog2 = new SaisanCog();
        assertThat(saisanCog1).isNotEqualTo(saisanCog2);

        saisanCog2.setId(saisanCog1.getId());
        assertThat(saisanCog1).isEqualTo(saisanCog2);

        saisanCog2 = getSaisanCogSample2();
        assertThat(saisanCog1).isNotEqualTo(saisanCog2);
    }
}
