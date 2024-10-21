package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlBestToothDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBestToothDTO.class);
        AlBestToothDTO alBestToothDTO1 = new AlBestToothDTO();
        alBestToothDTO1.setId(1L);
        AlBestToothDTO alBestToothDTO2 = new AlBestToothDTO();
        assertThat(alBestToothDTO1).isNotEqualTo(alBestToothDTO2);
        alBestToothDTO2.setId(alBestToothDTO1.getId());
        assertThat(alBestToothDTO1).isEqualTo(alBestToothDTO2);
        alBestToothDTO2.setId(2L);
        assertThat(alBestToothDTO1).isNotEqualTo(alBestToothDTO2);
        alBestToothDTO1.setId(null);
        assertThat(alBestToothDTO1).isNotEqualTo(alBestToothDTO2);
    }
}
