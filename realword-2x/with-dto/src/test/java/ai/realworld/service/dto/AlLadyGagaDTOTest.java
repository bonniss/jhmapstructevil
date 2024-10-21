package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlLadyGagaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLadyGagaDTO.class);
        AlLadyGagaDTO alLadyGagaDTO1 = new AlLadyGagaDTO();
        alLadyGagaDTO1.setId(UUID.randomUUID());
        AlLadyGagaDTO alLadyGagaDTO2 = new AlLadyGagaDTO();
        assertThat(alLadyGagaDTO1).isNotEqualTo(alLadyGagaDTO2);
        alLadyGagaDTO2.setId(alLadyGagaDTO1.getId());
        assertThat(alLadyGagaDTO1).isEqualTo(alLadyGagaDTO2);
        alLadyGagaDTO2.setId(UUID.randomUUID());
        assertThat(alLadyGagaDTO1).isNotEqualTo(alLadyGagaDTO2);
        alLadyGagaDTO1.setId(null);
        assertThat(alLadyGagaDTO1).isNotEqualTo(alLadyGagaDTO2);
    }
}
