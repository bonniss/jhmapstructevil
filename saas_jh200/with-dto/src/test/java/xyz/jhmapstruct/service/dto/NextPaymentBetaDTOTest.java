package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentBetaDTO.class);
        NextPaymentBetaDTO nextPaymentBetaDTO1 = new NextPaymentBetaDTO();
        nextPaymentBetaDTO1.setId(1L);
        NextPaymentBetaDTO nextPaymentBetaDTO2 = new NextPaymentBetaDTO();
        assertThat(nextPaymentBetaDTO1).isNotEqualTo(nextPaymentBetaDTO2);
        nextPaymentBetaDTO2.setId(nextPaymentBetaDTO1.getId());
        assertThat(nextPaymentBetaDTO1).isEqualTo(nextPaymentBetaDTO2);
        nextPaymentBetaDTO2.setId(2L);
        assertThat(nextPaymentBetaDTO1).isNotEqualTo(nextPaymentBetaDTO2);
        nextPaymentBetaDTO1.setId(null);
        assertThat(nextPaymentBetaDTO1).isNotEqualTo(nextPaymentBetaDTO2);
    }
}
