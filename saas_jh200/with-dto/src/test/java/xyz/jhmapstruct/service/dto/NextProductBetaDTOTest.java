package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductBetaDTO.class);
        NextProductBetaDTO nextProductBetaDTO1 = new NextProductBetaDTO();
        nextProductBetaDTO1.setId(1L);
        NextProductBetaDTO nextProductBetaDTO2 = new NextProductBetaDTO();
        assertThat(nextProductBetaDTO1).isNotEqualTo(nextProductBetaDTO2);
        nextProductBetaDTO2.setId(nextProductBetaDTO1.getId());
        assertThat(nextProductBetaDTO1).isEqualTo(nextProductBetaDTO2);
        nextProductBetaDTO2.setId(2L);
        assertThat(nextProductBetaDTO1).isNotEqualTo(nextProductBetaDTO2);
        nextProductBetaDTO1.setId(null);
        assertThat(nextProductBetaDTO1).isNotEqualTo(nextProductBetaDTO2);
    }
}
