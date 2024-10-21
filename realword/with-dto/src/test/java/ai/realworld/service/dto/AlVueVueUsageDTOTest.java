package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlVueVueUsageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueUsageDTO.class);
        AlVueVueUsageDTO alVueVueUsageDTO1 = new AlVueVueUsageDTO();
        alVueVueUsageDTO1.setId(UUID.randomUUID());
        AlVueVueUsageDTO alVueVueUsageDTO2 = new AlVueVueUsageDTO();
        assertThat(alVueVueUsageDTO1).isNotEqualTo(alVueVueUsageDTO2);
        alVueVueUsageDTO2.setId(alVueVueUsageDTO1.getId());
        assertThat(alVueVueUsageDTO1).isEqualTo(alVueVueUsageDTO2);
        alVueVueUsageDTO2.setId(UUID.randomUUID());
        assertThat(alVueVueUsageDTO1).isNotEqualTo(alVueVueUsageDTO2);
        alVueVueUsageDTO1.setId(null);
        assertThat(alVueVueUsageDTO1).isNotEqualTo(alVueVueUsageDTO2);
    }
}
