package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlVueVueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueDTO.class);
        AlVueVueDTO alVueVueDTO1 = new AlVueVueDTO();
        alVueVueDTO1.setId(UUID.randomUUID());
        AlVueVueDTO alVueVueDTO2 = new AlVueVueDTO();
        assertThat(alVueVueDTO1).isNotEqualTo(alVueVueDTO2);
        alVueVueDTO2.setId(alVueVueDTO1.getId());
        assertThat(alVueVueDTO1).isEqualTo(alVueVueDTO2);
        alVueVueDTO2.setId(UUID.randomUUID());
        assertThat(alVueVueDTO1).isNotEqualTo(alVueVueDTO2);
        alVueVueDTO1.setId(null);
        assertThat(alVueVueDTO1).isNotEqualTo(alVueVueDTO2);
    }
}
