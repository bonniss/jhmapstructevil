package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlPyuJokerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPyuJokerDTO.class);
        AlPyuJokerDTO alPyuJokerDTO1 = new AlPyuJokerDTO();
        alPyuJokerDTO1.setId(UUID.randomUUID());
        AlPyuJokerDTO alPyuJokerDTO2 = new AlPyuJokerDTO();
        assertThat(alPyuJokerDTO1).isNotEqualTo(alPyuJokerDTO2);
        alPyuJokerDTO2.setId(alPyuJokerDTO1.getId());
        assertThat(alPyuJokerDTO1).isEqualTo(alPyuJokerDTO2);
        alPyuJokerDTO2.setId(UUID.randomUUID());
        assertThat(alPyuJokerDTO1).isNotEqualTo(alPyuJokerDTO2);
        alPyuJokerDTO1.setId(null);
        assertThat(alPyuJokerDTO1).isNotEqualTo(alPyuJokerDTO2);
    }
}
