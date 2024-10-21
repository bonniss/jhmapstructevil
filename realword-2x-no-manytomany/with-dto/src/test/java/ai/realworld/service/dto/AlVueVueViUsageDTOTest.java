package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlVueVueViUsageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueViUsageDTO.class);
        AlVueVueViUsageDTO alVueVueViUsageDTO1 = new AlVueVueViUsageDTO();
        alVueVueViUsageDTO1.setId(UUID.randomUUID());
        AlVueVueViUsageDTO alVueVueViUsageDTO2 = new AlVueVueViUsageDTO();
        assertThat(alVueVueViUsageDTO1).isNotEqualTo(alVueVueViUsageDTO2);
        alVueVueViUsageDTO2.setId(alVueVueViUsageDTO1.getId());
        assertThat(alVueVueViUsageDTO1).isEqualTo(alVueVueViUsageDTO2);
        alVueVueViUsageDTO2.setId(UUID.randomUUID());
        assertThat(alVueVueViUsageDTO1).isNotEqualTo(alVueVueViUsageDTO2);
        alVueVueViUsageDTO1.setId(null);
        assertThat(alVueVueViUsageDTO1).isNotEqualTo(alVueVueViUsageDTO2);
    }
}
