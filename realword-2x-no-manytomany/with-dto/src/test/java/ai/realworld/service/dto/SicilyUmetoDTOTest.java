package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SicilyUmetoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SicilyUmetoDTO.class);
        SicilyUmetoDTO sicilyUmetoDTO1 = new SicilyUmetoDTO();
        sicilyUmetoDTO1.setId(1L);
        SicilyUmetoDTO sicilyUmetoDTO2 = new SicilyUmetoDTO();
        assertThat(sicilyUmetoDTO1).isNotEqualTo(sicilyUmetoDTO2);
        sicilyUmetoDTO2.setId(sicilyUmetoDTO1.getId());
        assertThat(sicilyUmetoDTO1).isEqualTo(sicilyUmetoDTO2);
        sicilyUmetoDTO2.setId(2L);
        assertThat(sicilyUmetoDTO1).isNotEqualTo(sicilyUmetoDTO2);
        sicilyUmetoDTO1.setId(null);
        assertThat(sicilyUmetoDTO1).isNotEqualTo(sicilyUmetoDTO2);
    }
}
