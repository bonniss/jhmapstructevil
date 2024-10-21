package ai.realworld.domain;

import static ai.realworld.domain.PamelaLouisViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PamelaLouisViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PamelaLouisVi.class);
        PamelaLouisVi pamelaLouisVi1 = getPamelaLouisViSample1();
        PamelaLouisVi pamelaLouisVi2 = new PamelaLouisVi();
        assertThat(pamelaLouisVi1).isNotEqualTo(pamelaLouisVi2);

        pamelaLouisVi2.setId(pamelaLouisVi1.getId());
        assertThat(pamelaLouisVi1).isEqualTo(pamelaLouisVi2);

        pamelaLouisVi2 = getPamelaLouisViSample2();
        assertThat(pamelaLouisVi1).isNotEqualTo(pamelaLouisVi2);
    }
}
