package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentGammaDTO.class);
        NextPaymentGammaDTO nextPaymentGammaDTO1 = new NextPaymentGammaDTO();
        nextPaymentGammaDTO1.setId(1L);
        NextPaymentGammaDTO nextPaymentGammaDTO2 = new NextPaymentGammaDTO();
        assertThat(nextPaymentGammaDTO1).isNotEqualTo(nextPaymentGammaDTO2);
        nextPaymentGammaDTO2.setId(nextPaymentGammaDTO1.getId());
        assertThat(nextPaymentGammaDTO1).isEqualTo(nextPaymentGammaDTO2);
        nextPaymentGammaDTO2.setId(2L);
        assertThat(nextPaymentGammaDTO1).isNotEqualTo(nextPaymentGammaDTO2);
        nextPaymentGammaDTO1.setId(null);
        assertThat(nextPaymentGammaDTO1).isNotEqualTo(nextPaymentGammaDTO2);
    }
}
