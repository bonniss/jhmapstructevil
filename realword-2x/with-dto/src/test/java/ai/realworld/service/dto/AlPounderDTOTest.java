package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPounderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPounderDTO.class);
        AlPounderDTO alPounderDTO1 = new AlPounderDTO();
        alPounderDTO1.setId(1L);
        AlPounderDTO alPounderDTO2 = new AlPounderDTO();
        assertThat(alPounderDTO1).isNotEqualTo(alPounderDTO2);
        alPounderDTO2.setId(alPounderDTO1.getId());
        assertThat(alPounderDTO1).isEqualTo(alPounderDTO2);
        alPounderDTO2.setId(2L);
        assertThat(alPounderDTO1).isNotEqualTo(alPounderDTO2);
        alPounderDTO1.setId(null);
        assertThat(alPounderDTO1).isNotEqualTo(alPounderDTO2);
    }
}
