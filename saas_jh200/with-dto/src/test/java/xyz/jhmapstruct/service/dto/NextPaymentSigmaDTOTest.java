package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentSigmaDTO.class);
        NextPaymentSigmaDTO nextPaymentSigmaDTO1 = new NextPaymentSigmaDTO();
        nextPaymentSigmaDTO1.setId(1L);
        NextPaymentSigmaDTO nextPaymentSigmaDTO2 = new NextPaymentSigmaDTO();
        assertThat(nextPaymentSigmaDTO1).isNotEqualTo(nextPaymentSigmaDTO2);
        nextPaymentSigmaDTO2.setId(nextPaymentSigmaDTO1.getId());
        assertThat(nextPaymentSigmaDTO1).isEqualTo(nextPaymentSigmaDTO2);
        nextPaymentSigmaDTO2.setId(2L);
        assertThat(nextPaymentSigmaDTO1).isNotEqualTo(nextPaymentSigmaDTO2);
        nextPaymentSigmaDTO1.setId(null);
        assertThat(nextPaymentSigmaDTO1).isNotEqualTo(nextPaymentSigmaDTO2);
    }
}
