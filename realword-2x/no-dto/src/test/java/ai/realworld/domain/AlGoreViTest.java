package ai.realworld.domain;

import static ai.realworld.domain.AlGoreViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoreViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoreVi.class);
        AlGoreVi alGoreVi1 = getAlGoreViSample1();
        AlGoreVi alGoreVi2 = new AlGoreVi();
        assertThat(alGoreVi1).isNotEqualTo(alGoreVi2);

        alGoreVi2.setId(alGoreVi1.getId());
        assertThat(alGoreVi1).isEqualTo(alGoreVi2);

        alGoreVi2 = getAlGoreViSample2();
        assertThat(alGoreVi1).isNotEqualTo(alGoreVi2);
    }
}
