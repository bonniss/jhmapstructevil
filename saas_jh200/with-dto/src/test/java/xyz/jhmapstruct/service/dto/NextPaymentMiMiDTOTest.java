package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentMiMiDTO.class);
        NextPaymentMiMiDTO nextPaymentMiMiDTO1 = new NextPaymentMiMiDTO();
        nextPaymentMiMiDTO1.setId(1L);
        NextPaymentMiMiDTO nextPaymentMiMiDTO2 = new NextPaymentMiMiDTO();
        assertThat(nextPaymentMiMiDTO1).isNotEqualTo(nextPaymentMiMiDTO2);
        nextPaymentMiMiDTO2.setId(nextPaymentMiMiDTO1.getId());
        assertThat(nextPaymentMiMiDTO1).isEqualTo(nextPaymentMiMiDTO2);
        nextPaymentMiMiDTO2.setId(2L);
        assertThat(nextPaymentMiMiDTO1).isNotEqualTo(nextPaymentMiMiDTO2);
        nextPaymentMiMiDTO1.setId(null);
        assertThat(nextPaymentMiMiDTO1).isNotEqualTo(nextPaymentMiMiDTO2);
    }
}
