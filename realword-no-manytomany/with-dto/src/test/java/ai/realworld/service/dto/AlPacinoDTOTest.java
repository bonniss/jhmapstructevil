package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlPacinoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoDTO.class);
        AlPacinoDTO alPacinoDTO1 = new AlPacinoDTO();
        alPacinoDTO1.setId(UUID.randomUUID());
        AlPacinoDTO alPacinoDTO2 = new AlPacinoDTO();
        assertThat(alPacinoDTO1).isNotEqualTo(alPacinoDTO2);
        alPacinoDTO2.setId(alPacinoDTO1.getId());
        assertThat(alPacinoDTO1).isEqualTo(alPacinoDTO2);
        alPacinoDTO2.setId(UUID.randomUUID());
        assertThat(alPacinoDTO1).isNotEqualTo(alPacinoDTO2);
        alPacinoDTO1.setId(null);
        assertThat(alPacinoDTO1).isNotEqualTo(alPacinoDTO2);
    }
}
