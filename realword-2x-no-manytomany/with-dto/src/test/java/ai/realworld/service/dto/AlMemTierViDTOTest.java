package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlMemTierViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMemTierViDTO.class);
        AlMemTierViDTO alMemTierViDTO1 = new AlMemTierViDTO();
        alMemTierViDTO1.setId(1L);
        AlMemTierViDTO alMemTierViDTO2 = new AlMemTierViDTO();
        assertThat(alMemTierViDTO1).isNotEqualTo(alMemTierViDTO2);
        alMemTierViDTO2.setId(alMemTierViDTO1.getId());
        assertThat(alMemTierViDTO1).isEqualTo(alMemTierViDTO2);
        alMemTierViDTO2.setId(2L);
        assertThat(alMemTierViDTO1).isNotEqualTo(alMemTierViDTO2);
        alMemTierViDTO1.setId(null);
        assertThat(alMemTierViDTO1).isNotEqualTo(alMemTierViDTO2);
    }
}
