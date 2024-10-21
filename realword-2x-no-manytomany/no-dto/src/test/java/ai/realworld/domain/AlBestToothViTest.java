package ai.realworld.domain;

import static ai.realworld.domain.AlBestToothViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlBestToothViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBestToothVi.class);
        AlBestToothVi alBestToothVi1 = getAlBestToothViSample1();
        AlBestToothVi alBestToothVi2 = new AlBestToothVi();
        assertThat(alBestToothVi1).isNotEqualTo(alBestToothVi2);

        alBestToothVi2.setId(alBestToothVi1.getId());
        assertThat(alBestToothVi1).isEqualTo(alBestToothVi2);

        alBestToothVi2 = getAlBestToothViSample2();
        assertThat(alBestToothVi1).isNotEqualTo(alBestToothVi2);
    }
}
