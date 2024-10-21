package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlProProViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlProProViDTO.class);
        AlProProViDTO alProProViDTO1 = new AlProProViDTO();
        alProProViDTO1.setId(UUID.randomUUID());
        AlProProViDTO alProProViDTO2 = new AlProProViDTO();
        assertThat(alProProViDTO1).isNotEqualTo(alProProViDTO2);
        alProProViDTO2.setId(alProProViDTO1.getId());
        assertThat(alProProViDTO1).isEqualTo(alProProViDTO2);
        alProProViDTO2.setId(UUID.randomUUID());
        assertThat(alProProViDTO1).isNotEqualTo(alProProViDTO2);
        alProProViDTO1.setId(null);
        assertThat(alProProViDTO1).isNotEqualTo(alProProViDTO2);
    }
}
