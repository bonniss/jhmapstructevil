package ai.realworld.domain;

import static ai.realworld.domain.MagharettiViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MagharettiViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MagharettiVi.class);
        MagharettiVi magharettiVi1 = getMagharettiViSample1();
        MagharettiVi magharettiVi2 = new MagharettiVi();
        assertThat(magharettiVi1).isNotEqualTo(magharettiVi2);

        magharettiVi2.setId(magharettiVi1.getId());
        assertThat(magharettiVi1).isEqualTo(magharettiVi2);

        magharettiVi2 = getMagharettiViSample2();
        assertThat(magharettiVi1).isNotEqualTo(magharettiVi2);
    }
}
