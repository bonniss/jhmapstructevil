package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OlMasterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OlMasterDTO.class);
        OlMasterDTO olMasterDTO1 = new OlMasterDTO();
        olMasterDTO1.setId(UUID.randomUUID());
        OlMasterDTO olMasterDTO2 = new OlMasterDTO();
        assertThat(olMasterDTO1).isNotEqualTo(olMasterDTO2);
        olMasterDTO2.setId(olMasterDTO1.getId());
        assertThat(olMasterDTO1).isEqualTo(olMasterDTO2);
        olMasterDTO2.setId(UUID.randomUUID());
        assertThat(olMasterDTO1).isNotEqualTo(olMasterDTO2);
        olMasterDTO1.setId(null);
        assertThat(olMasterDTO1).isNotEqualTo(olMasterDTO2);
    }
}
