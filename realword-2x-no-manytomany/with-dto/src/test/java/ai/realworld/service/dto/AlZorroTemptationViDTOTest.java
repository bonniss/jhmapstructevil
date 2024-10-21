package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlZorroTemptationViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlZorroTemptationViDTO.class);
        AlZorroTemptationViDTO alZorroTemptationViDTO1 = new AlZorroTemptationViDTO();
        alZorroTemptationViDTO1.setId(1L);
        AlZorroTemptationViDTO alZorroTemptationViDTO2 = new AlZorroTemptationViDTO();
        assertThat(alZorroTemptationViDTO1).isNotEqualTo(alZorroTemptationViDTO2);
        alZorroTemptationViDTO2.setId(alZorroTemptationViDTO1.getId());
        assertThat(alZorroTemptationViDTO1).isEqualTo(alZorroTemptationViDTO2);
        alZorroTemptationViDTO2.setId(2L);
        assertThat(alZorroTemptationViDTO1).isNotEqualTo(alZorroTemptationViDTO2);
        alZorroTemptationViDTO1.setId(null);
        assertThat(alZorroTemptationViDTO1).isNotEqualTo(alZorroTemptationViDTO2);
    }
}
