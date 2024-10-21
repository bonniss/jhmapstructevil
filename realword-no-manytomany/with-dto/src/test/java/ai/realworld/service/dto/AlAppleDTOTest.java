package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlAppleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlAppleDTO.class);
        AlAppleDTO alAppleDTO1 = new AlAppleDTO();
        alAppleDTO1.setId(UUID.randomUUID());
        AlAppleDTO alAppleDTO2 = new AlAppleDTO();
        assertThat(alAppleDTO1).isNotEqualTo(alAppleDTO2);
        alAppleDTO2.setId(alAppleDTO1.getId());
        assertThat(alAppleDTO1).isEqualTo(alAppleDTO2);
        alAppleDTO2.setId(UUID.randomUUID());
        assertThat(alAppleDTO1).isNotEqualTo(alAppleDTO2);
        alAppleDTO1.setId(null);
        assertThat(alAppleDTO1).isNotEqualTo(alAppleDTO2);
    }
}
