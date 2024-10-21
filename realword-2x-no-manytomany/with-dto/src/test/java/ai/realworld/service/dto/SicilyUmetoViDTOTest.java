package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SicilyUmetoViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SicilyUmetoViDTO.class);
        SicilyUmetoViDTO sicilyUmetoViDTO1 = new SicilyUmetoViDTO();
        sicilyUmetoViDTO1.setId(1L);
        SicilyUmetoViDTO sicilyUmetoViDTO2 = new SicilyUmetoViDTO();
        assertThat(sicilyUmetoViDTO1).isNotEqualTo(sicilyUmetoViDTO2);
        sicilyUmetoViDTO2.setId(sicilyUmetoViDTO1.getId());
        assertThat(sicilyUmetoViDTO1).isEqualTo(sicilyUmetoViDTO2);
        sicilyUmetoViDTO2.setId(2L);
        assertThat(sicilyUmetoViDTO1).isNotEqualTo(sicilyUmetoViDTO2);
        sicilyUmetoViDTO1.setId(null);
        assertThat(sicilyUmetoViDTO1).isNotEqualTo(sicilyUmetoViDTO2);
    }
}
