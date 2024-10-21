package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlPyuJokerViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuJokerViDTO.class);
        AlPyuJokerViDTO alPyuJokerViDTO1 = new AlPyuJokerViDTO();
        alPyuJokerViDTO1.setId(UUID.randomUUID());
        AlPyuJokerViDTO alPyuJokerViDTO2 = new AlPyuJokerViDTO();
        assertThat(alPyuJokerViDTO1).isNotEqualTo(alPyuJokerViDTO2);
        alPyuJokerViDTO2.setId(alPyuJokerViDTO1.getId());
        assertThat(alPyuJokerViDTO1).isEqualTo(alPyuJokerViDTO2);
        alPyuJokerViDTO2.setId(UUID.randomUUID());
        assertThat(alPyuJokerViDTO1).isNotEqualTo(alPyuJokerViDTO2);
        alPyuJokerViDTO1.setId(null);
        assertThat(alPyuJokerViDTO1).isNotEqualTo(alPyuJokerViDTO2);
    }
}
