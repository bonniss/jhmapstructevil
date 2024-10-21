package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InitiumViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InitiumViDTO.class);
        InitiumViDTO initiumViDTO1 = new InitiumViDTO();
        initiumViDTO1.setId(1L);
        InitiumViDTO initiumViDTO2 = new InitiumViDTO();
        assertThat(initiumViDTO1).isNotEqualTo(initiumViDTO2);
        initiumViDTO2.setId(initiumViDTO1.getId());
        assertThat(initiumViDTO1).isEqualTo(initiumViDTO2);
        initiumViDTO2.setId(2L);
        assertThat(initiumViDTO1).isNotEqualTo(initiumViDTO2);
        initiumViDTO1.setId(null);
        assertThat(initiumViDTO1).isNotEqualTo(initiumViDTO2);
    }
}
