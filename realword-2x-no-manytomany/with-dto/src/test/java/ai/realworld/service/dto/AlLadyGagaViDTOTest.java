package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlLadyGagaViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLadyGagaViDTO.class);
        AlLadyGagaViDTO alLadyGagaViDTO1 = new AlLadyGagaViDTO();
        alLadyGagaViDTO1.setId(UUID.randomUUID());
        AlLadyGagaViDTO alLadyGagaViDTO2 = new AlLadyGagaViDTO();
        assertThat(alLadyGagaViDTO1).isNotEqualTo(alLadyGagaViDTO2);
        alLadyGagaViDTO2.setId(alLadyGagaViDTO1.getId());
        assertThat(alLadyGagaViDTO1).isEqualTo(alLadyGagaViDTO2);
        alLadyGagaViDTO2.setId(UUID.randomUUID());
        assertThat(alLadyGagaViDTO1).isNotEqualTo(alLadyGagaViDTO2);
        alLadyGagaViDTO1.setId(null);
        assertThat(alLadyGagaViDTO1).isNotEqualTo(alLadyGagaViDTO2);
    }
}
