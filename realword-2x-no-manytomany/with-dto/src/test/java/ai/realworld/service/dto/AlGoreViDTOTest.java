package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoreViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoreViDTO.class);
        AlGoreViDTO alGoreViDTO1 = new AlGoreViDTO();
        alGoreViDTO1.setId(1L);
        AlGoreViDTO alGoreViDTO2 = new AlGoreViDTO();
        assertThat(alGoreViDTO1).isNotEqualTo(alGoreViDTO2);
        alGoreViDTO2.setId(alGoreViDTO1.getId());
        assertThat(alGoreViDTO1).isEqualTo(alGoreViDTO2);
        alGoreViDTO2.setId(2L);
        assertThat(alGoreViDTO1).isNotEqualTo(alGoreViDTO2);
        alGoreViDTO1.setId(null);
        assertThat(alGoreViDTO1).isNotEqualTo(alGoreViDTO2);
    }
}
