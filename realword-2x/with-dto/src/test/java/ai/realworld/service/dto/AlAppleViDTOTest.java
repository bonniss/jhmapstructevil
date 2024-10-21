package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlAppleViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlAppleViDTO.class);
        AlAppleViDTO alAppleViDTO1 = new AlAppleViDTO();
        alAppleViDTO1.setId(UUID.randomUUID());
        AlAppleViDTO alAppleViDTO2 = new AlAppleViDTO();
        assertThat(alAppleViDTO1).isNotEqualTo(alAppleViDTO2);
        alAppleViDTO2.setId(alAppleViDTO1.getId());
        assertThat(alAppleViDTO1).isEqualTo(alAppleViDTO2);
        alAppleViDTO2.setId(UUID.randomUUID());
        assertThat(alAppleViDTO1).isNotEqualTo(alAppleViDTO2);
        alAppleViDTO1.setId(null);
        assertThat(alAppleViDTO1).isNotEqualTo(alAppleViDTO2);
    }
}
