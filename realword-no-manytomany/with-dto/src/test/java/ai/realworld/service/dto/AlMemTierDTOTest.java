package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlMemTierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlMemTierDTO.class);
        AlMemTierDTO alMemTierDTO1 = new AlMemTierDTO();
        alMemTierDTO1.setId(1L);
        AlMemTierDTO alMemTierDTO2 = new AlMemTierDTO();
        assertThat(alMemTierDTO1).isNotEqualTo(alMemTierDTO2);
        alMemTierDTO2.setId(alMemTierDTO1.getId());
        assertThat(alMemTierDTO1).isEqualTo(alMemTierDTO2);
        alMemTierDTO2.setId(2L);
        assertThat(alMemTierDTO1).isNotEqualTo(alMemTierDTO2);
        alMemTierDTO1.setId(null);
        assertThat(alMemTierDTO1).isNotEqualTo(alMemTierDTO2);
    }
}
