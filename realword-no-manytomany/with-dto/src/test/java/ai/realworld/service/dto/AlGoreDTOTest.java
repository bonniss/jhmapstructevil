package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlGoreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlGoreDTO.class);
        AlGoreDTO alGoreDTO1 = new AlGoreDTO();
        alGoreDTO1.setId(1L);
        AlGoreDTO alGoreDTO2 = new AlGoreDTO();
        assertThat(alGoreDTO1).isNotEqualTo(alGoreDTO2);
        alGoreDTO2.setId(alGoreDTO1.getId());
        assertThat(alGoreDTO1).isEqualTo(alGoreDTO2);
        alGoreDTO2.setId(2L);
        assertThat(alGoreDTO1).isNotEqualTo(alGoreDTO2);
        alGoreDTO1.setId(null);
        assertThat(alGoreDTO1).isNotEqualTo(alGoreDTO2);
    }
}
