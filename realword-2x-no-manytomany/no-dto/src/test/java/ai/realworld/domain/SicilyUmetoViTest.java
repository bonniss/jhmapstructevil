package ai.realworld.domain;

import static ai.realworld.domain.SicilyUmetoViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SicilyUmetoViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SicilyUmetoVi.class);
        SicilyUmetoVi sicilyUmetoVi1 = getSicilyUmetoViSample1();
        SicilyUmetoVi sicilyUmetoVi2 = new SicilyUmetoVi();
        assertThat(sicilyUmetoVi1).isNotEqualTo(sicilyUmetoVi2);

        sicilyUmetoVi2.setId(sicilyUmetoVi1.getId());
        assertThat(sicilyUmetoVi1).isEqualTo(sicilyUmetoVi2);

        sicilyUmetoVi2 = getSicilyUmetoViSample2();
        assertThat(sicilyUmetoVi1).isNotEqualTo(sicilyUmetoVi2);
    }
}
