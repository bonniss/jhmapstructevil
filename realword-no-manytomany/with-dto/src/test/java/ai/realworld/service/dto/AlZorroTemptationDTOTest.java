package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlZorroTemptationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlZorroTemptationDTO.class);
        AlZorroTemptationDTO alZorroTemptationDTO1 = new AlZorroTemptationDTO();
        alZorroTemptationDTO1.setId(1L);
        AlZorroTemptationDTO alZorroTemptationDTO2 = new AlZorroTemptationDTO();
        assertThat(alZorroTemptationDTO1).isNotEqualTo(alZorroTemptationDTO2);
        alZorroTemptationDTO2.setId(alZorroTemptationDTO1.getId());
        assertThat(alZorroTemptationDTO1).isEqualTo(alZorroTemptationDTO2);
        alZorroTemptationDTO2.setId(2L);
        assertThat(alZorroTemptationDTO1).isNotEqualTo(alZorroTemptationDTO2);
        alZorroTemptationDTO1.setId(null);
        assertThat(alZorroTemptationDTO1).isNotEqualTo(alZorroTemptationDTO2);
    }
}
