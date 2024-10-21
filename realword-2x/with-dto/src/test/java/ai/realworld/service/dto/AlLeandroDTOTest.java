package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlLeandroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLeandroDTO.class);
        AlLeandroDTO alLeandroDTO1 = new AlLeandroDTO();
        alLeandroDTO1.setId(UUID.randomUUID());
        AlLeandroDTO alLeandroDTO2 = new AlLeandroDTO();
        assertThat(alLeandroDTO1).isNotEqualTo(alLeandroDTO2);
        alLeandroDTO2.setId(alLeandroDTO1.getId());
        assertThat(alLeandroDTO1).isEqualTo(alLeandroDTO2);
        alLeandroDTO2.setId(UUID.randomUUID());
        assertThat(alLeandroDTO1).isNotEqualTo(alLeandroDTO2);
        alLeandroDTO1.setId(null);
        assertThat(alLeandroDTO1).isNotEqualTo(alLeandroDTO2);
    }
}
