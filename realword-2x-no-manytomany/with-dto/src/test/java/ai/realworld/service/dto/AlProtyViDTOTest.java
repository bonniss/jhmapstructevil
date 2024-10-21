package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlProtyViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlProtyViDTO.class);
        AlProtyViDTO alProtyViDTO1 = new AlProtyViDTO();
        alProtyViDTO1.setId(UUID.randomUUID());
        AlProtyViDTO alProtyViDTO2 = new AlProtyViDTO();
        assertThat(alProtyViDTO1).isNotEqualTo(alProtyViDTO2);
        alProtyViDTO2.setId(alProtyViDTO1.getId());
        assertThat(alProtyViDTO1).isEqualTo(alProtyViDTO2);
        alProtyViDTO2.setId(UUID.randomUUID());
        assertThat(alProtyViDTO1).isNotEqualTo(alProtyViDTO2);
        alProtyViDTO1.setId(null);
        assertThat(alProtyViDTO1).isNotEqualTo(alProtyViDTO2);
    }
}
