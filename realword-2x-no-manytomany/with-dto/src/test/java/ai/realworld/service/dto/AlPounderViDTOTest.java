package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPounderViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPounderViDTO.class);
        AlPounderViDTO alPounderViDTO1 = new AlPounderViDTO();
        alPounderViDTO1.setId(1L);
        AlPounderViDTO alPounderViDTO2 = new AlPounderViDTO();
        assertThat(alPounderViDTO1).isNotEqualTo(alPounderViDTO2);
        alPounderViDTO2.setId(alPounderViDTO1.getId());
        assertThat(alPounderViDTO1).isEqualTo(alPounderViDTO2);
        alPounderViDTO2.setId(2L);
        assertThat(alPounderViDTO1).isNotEqualTo(alPounderViDTO2);
        alPounderViDTO1.setId(null);
        assertThat(alPounderViDTO1).isNotEqualTo(alPounderViDTO2);
    }
}
