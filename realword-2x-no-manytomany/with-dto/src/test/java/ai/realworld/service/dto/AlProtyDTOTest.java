package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlProtyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlProtyDTO.class);
        AlProtyDTO alProtyDTO1 = new AlProtyDTO();
        alProtyDTO1.setId(UUID.randomUUID());
        AlProtyDTO alProtyDTO2 = new AlProtyDTO();
        assertThat(alProtyDTO1).isNotEqualTo(alProtyDTO2);
        alProtyDTO2.setId(alProtyDTO1.getId());
        assertThat(alProtyDTO1).isEqualTo(alProtyDTO2);
        alProtyDTO2.setId(UUID.randomUUID());
        assertThat(alProtyDTO1).isNotEqualTo(alProtyDTO2);
        alProtyDTO1.setId(null);
        assertThat(alProtyDTO1).isNotEqualTo(alProtyDTO2);
    }
}
