package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlDesireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlDesireDTO.class);
        AlDesireDTO alDesireDTO1 = new AlDesireDTO();
        alDesireDTO1.setId(UUID.randomUUID());
        AlDesireDTO alDesireDTO2 = new AlDesireDTO();
        assertThat(alDesireDTO1).isNotEqualTo(alDesireDTO2);
        alDesireDTO2.setId(alDesireDTO1.getId());
        assertThat(alDesireDTO1).isEqualTo(alDesireDTO2);
        alDesireDTO2.setId(UUID.randomUUID());
        assertThat(alDesireDTO1).isNotEqualTo(alDesireDTO2);
        alDesireDTO1.setId(null);
        assertThat(alDesireDTO1).isNotEqualTo(alDesireDTO2);
    }
}
