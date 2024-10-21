package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlActisoViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlActisoViDTO.class);
        AlActisoViDTO alActisoViDTO1 = new AlActisoViDTO();
        alActisoViDTO1.setId(1L);
        AlActisoViDTO alActisoViDTO2 = new AlActisoViDTO();
        assertThat(alActisoViDTO1).isNotEqualTo(alActisoViDTO2);
        alActisoViDTO2.setId(alActisoViDTO1.getId());
        assertThat(alActisoViDTO1).isEqualTo(alActisoViDTO2);
        alActisoViDTO2.setId(2L);
        assertThat(alActisoViDTO1).isNotEqualTo(alActisoViDTO2);
        alActisoViDTO1.setId(null);
        assertThat(alActisoViDTO1).isNotEqualTo(alActisoViDTO2);
    }
}
