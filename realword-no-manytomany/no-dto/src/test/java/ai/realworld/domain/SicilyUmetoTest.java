package ai.realworld.domain;

import static ai.realworld.domain.SicilyUmetoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SicilyUmetoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SicilyUmeto.class);
        SicilyUmeto sicilyUmeto1 = getSicilyUmetoSample1();
        SicilyUmeto sicilyUmeto2 = new SicilyUmeto();
        assertThat(sicilyUmeto1).isNotEqualTo(sicilyUmeto2);

        sicilyUmeto2.setId(sicilyUmeto1.getId());
        assertThat(sicilyUmeto1).isEqualTo(sicilyUmeto2);

        sicilyUmeto2 = getSicilyUmetoSample2();
        assertThat(sicilyUmeto1).isNotEqualTo(sicilyUmeto2);
    }
}
