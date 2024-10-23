package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderBetaDTO.class);
        NextOrderBetaDTO nextOrderBetaDTO1 = new NextOrderBetaDTO();
        nextOrderBetaDTO1.setId(1L);
        NextOrderBetaDTO nextOrderBetaDTO2 = new NextOrderBetaDTO();
        assertThat(nextOrderBetaDTO1).isNotEqualTo(nextOrderBetaDTO2);
        nextOrderBetaDTO2.setId(nextOrderBetaDTO1.getId());
        assertThat(nextOrderBetaDTO1).isEqualTo(nextOrderBetaDTO2);
        nextOrderBetaDTO2.setId(2L);
        assertThat(nextOrderBetaDTO1).isNotEqualTo(nextOrderBetaDTO2);
        nextOrderBetaDTO1.setId(null);
        assertThat(nextOrderBetaDTO1).isNotEqualTo(nextOrderBetaDTO2);
    }
}
