package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlAlexTypeViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlAlexTypeViDTO.class);
        AlAlexTypeViDTO alAlexTypeViDTO1 = new AlAlexTypeViDTO();
        alAlexTypeViDTO1.setId(UUID.randomUUID());
        AlAlexTypeViDTO alAlexTypeViDTO2 = new AlAlexTypeViDTO();
        assertThat(alAlexTypeViDTO1).isNotEqualTo(alAlexTypeViDTO2);
        alAlexTypeViDTO2.setId(alAlexTypeViDTO1.getId());
        assertThat(alAlexTypeViDTO1).isEqualTo(alAlexTypeViDTO2);
        alAlexTypeViDTO2.setId(UUID.randomUUID());
        assertThat(alAlexTypeViDTO1).isNotEqualTo(alAlexTypeViDTO2);
        alAlexTypeViDTO1.setId(null);
        assertThat(alAlexTypeViDTO1).isNotEqualTo(alAlexTypeViDTO2);
    }
}
