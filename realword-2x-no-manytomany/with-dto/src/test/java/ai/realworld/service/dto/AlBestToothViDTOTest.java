package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlBestToothViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBestToothViDTO.class);
        AlBestToothViDTO alBestToothViDTO1 = new AlBestToothViDTO();
        alBestToothViDTO1.setId(1L);
        AlBestToothViDTO alBestToothViDTO2 = new AlBestToothViDTO();
        assertThat(alBestToothViDTO1).isNotEqualTo(alBestToothViDTO2);
        alBestToothViDTO2.setId(alBestToothViDTO1.getId());
        assertThat(alBestToothViDTO1).isEqualTo(alBestToothViDTO2);
        alBestToothViDTO2.setId(2L);
        assertThat(alBestToothViDTO1).isNotEqualTo(alBestToothViDTO2);
        alBestToothViDTO1.setId(null);
        assertThat(alBestToothViDTO1).isNotEqualTo(alBestToothViDTO2);
    }
}
