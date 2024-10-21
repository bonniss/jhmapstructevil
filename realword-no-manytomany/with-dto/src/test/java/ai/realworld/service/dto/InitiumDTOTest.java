package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InitiumDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InitiumDTO.class);
        InitiumDTO initiumDTO1 = new InitiumDTO();
        initiumDTO1.setId(1L);
        InitiumDTO initiumDTO2 = new InitiumDTO();
        assertThat(initiumDTO1).isNotEqualTo(initiumDTO2);
        initiumDTO2.setId(initiumDTO1.getId());
        assertThat(initiumDTO1).isEqualTo(initiumDTO2);
        initiumDTO2.setId(2L);
        assertThat(initiumDTO1).isNotEqualTo(initiumDTO2);
        initiumDTO1.setId(null);
        assertThat(initiumDTO1).isNotEqualTo(initiumDTO2);
    }
}
