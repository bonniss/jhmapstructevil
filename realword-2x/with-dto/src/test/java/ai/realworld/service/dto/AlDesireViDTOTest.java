package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlDesireViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlDesireViDTO.class);
        AlDesireViDTO alDesireViDTO1 = new AlDesireViDTO();
        alDesireViDTO1.setId(UUID.randomUUID());
        AlDesireViDTO alDesireViDTO2 = new AlDesireViDTO();
        assertThat(alDesireViDTO1).isNotEqualTo(alDesireViDTO2);
        alDesireViDTO2.setId(alDesireViDTO1.getId());
        assertThat(alDesireViDTO1).isEqualTo(alDesireViDTO2);
        alDesireViDTO2.setId(UUID.randomUUID());
        assertThat(alDesireViDTO1).isNotEqualTo(alDesireViDTO2);
        alDesireViDTO1.setId(null);
        assertThat(alDesireViDTO1).isNotEqualTo(alDesireViDTO2);
    }
}
