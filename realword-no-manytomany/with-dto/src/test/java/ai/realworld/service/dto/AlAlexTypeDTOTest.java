package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlAlexTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlAlexTypeDTO.class);
        AlAlexTypeDTO alAlexTypeDTO1 = new AlAlexTypeDTO();
        alAlexTypeDTO1.setId(UUID.randomUUID());
        AlAlexTypeDTO alAlexTypeDTO2 = new AlAlexTypeDTO();
        assertThat(alAlexTypeDTO1).isNotEqualTo(alAlexTypeDTO2);
        alAlexTypeDTO2.setId(alAlexTypeDTO1.getId());
        assertThat(alAlexTypeDTO1).isEqualTo(alAlexTypeDTO2);
        alAlexTypeDTO2.setId(UUID.randomUUID());
        assertThat(alAlexTypeDTO1).isNotEqualTo(alAlexTypeDTO2);
        alAlexTypeDTO1.setId(null);
        assertThat(alAlexTypeDTO1).isNotEqualTo(alAlexTypeDTO2);
    }
}
