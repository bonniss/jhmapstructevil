package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OlAlmantinoMiloDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OlAlmantinoMiloDTO.class);
        OlAlmantinoMiloDTO olAlmantinoMiloDTO1 = new OlAlmantinoMiloDTO();
        olAlmantinoMiloDTO1.setId(UUID.randomUUID());
        OlAlmantinoMiloDTO olAlmantinoMiloDTO2 = new OlAlmantinoMiloDTO();
        assertThat(olAlmantinoMiloDTO1).isNotEqualTo(olAlmantinoMiloDTO2);
        olAlmantinoMiloDTO2.setId(olAlmantinoMiloDTO1.getId());
        assertThat(olAlmantinoMiloDTO1).isEqualTo(olAlmantinoMiloDTO2);
        olAlmantinoMiloDTO2.setId(UUID.randomUUID());
        assertThat(olAlmantinoMiloDTO1).isNotEqualTo(olAlmantinoMiloDTO2);
        olAlmantinoMiloDTO1.setId(null);
        assertThat(olAlmantinoMiloDTO1).isNotEqualTo(olAlmantinoMiloDTO2);
    }
}
