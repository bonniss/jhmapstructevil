package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JohnLennonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JohnLennonDTO.class);
        JohnLennonDTO johnLennonDTO1 = new JohnLennonDTO();
        johnLennonDTO1.setId(UUID.randomUUID());
        JohnLennonDTO johnLennonDTO2 = new JohnLennonDTO();
        assertThat(johnLennonDTO1).isNotEqualTo(johnLennonDTO2);
        johnLennonDTO2.setId(johnLennonDTO1.getId());
        assertThat(johnLennonDTO1).isEqualTo(johnLennonDTO2);
        johnLennonDTO2.setId(UUID.randomUUID());
        assertThat(johnLennonDTO1).isNotEqualTo(johnLennonDTO2);
        johnLennonDTO1.setId(null);
        assertThat(johnLennonDTO1).isNotEqualTo(johnLennonDTO2);
    }
}
