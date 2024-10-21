package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlVueVueViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueViDTO.class);
        AlVueVueViDTO alVueVueViDTO1 = new AlVueVueViDTO();
        alVueVueViDTO1.setId(UUID.randomUUID());
        AlVueVueViDTO alVueVueViDTO2 = new AlVueVueViDTO();
        assertThat(alVueVueViDTO1).isNotEqualTo(alVueVueViDTO2);
        alVueVueViDTO2.setId(alVueVueViDTO1.getId());
        assertThat(alVueVueViDTO1).isEqualTo(alVueVueViDTO2);
        alVueVueViDTO2.setId(UUID.randomUUID());
        assertThat(alVueVueViDTO1).isNotEqualTo(alVueVueViDTO2);
        alVueVueViDTO1.setId(null);
        assertThat(alVueVueViDTO1).isNotEqualTo(alVueVueViDTO2);
    }
}
