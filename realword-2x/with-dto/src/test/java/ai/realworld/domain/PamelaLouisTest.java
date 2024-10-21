package ai.realworld.domain;

import static ai.realworld.domain.PamelaLouisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PamelaLouisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PamelaLouis.class);
        PamelaLouis pamelaLouis1 = getPamelaLouisSample1();
        PamelaLouis pamelaLouis2 = new PamelaLouis();
        assertThat(pamelaLouis1).isNotEqualTo(pamelaLouis2);

        pamelaLouis2.setId(pamelaLouis1.getId());
        assertThat(pamelaLouis1).isEqualTo(pamelaLouis2);

        pamelaLouis2 = getPamelaLouisSample2();
        assertThat(pamelaLouis1).isNotEqualTo(pamelaLouis2);
    }
}
