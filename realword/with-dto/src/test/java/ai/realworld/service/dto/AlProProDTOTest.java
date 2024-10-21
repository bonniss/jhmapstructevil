package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlProProDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlProProDTO.class);
        AlProProDTO alProProDTO1 = new AlProProDTO();
        alProProDTO1.setId(UUID.randomUUID());
        AlProProDTO alProProDTO2 = new AlProProDTO();
        assertThat(alProProDTO1).isNotEqualTo(alProProDTO2);
        alProProDTO2.setId(alProProDTO1.getId());
        assertThat(alProProDTO1).isEqualTo(alProProDTO2);
        alProProDTO2.setId(UUID.randomUUID());
        assertThat(alProProDTO1).isNotEqualTo(alProProDTO2);
        alProProDTO1.setId(null);
        assertThat(alProProDTO1).isNotEqualTo(alProProDTO2);
    }
}
