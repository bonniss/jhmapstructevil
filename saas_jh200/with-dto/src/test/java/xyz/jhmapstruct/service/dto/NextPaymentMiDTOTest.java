package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentMiDTO.class);
        NextPaymentMiDTO nextPaymentMiDTO1 = new NextPaymentMiDTO();
        nextPaymentMiDTO1.setId(1L);
        NextPaymentMiDTO nextPaymentMiDTO2 = new NextPaymentMiDTO();
        assertThat(nextPaymentMiDTO1).isNotEqualTo(nextPaymentMiDTO2);
        nextPaymentMiDTO2.setId(nextPaymentMiDTO1.getId());
        assertThat(nextPaymentMiDTO1).isEqualTo(nextPaymentMiDTO2);
        nextPaymentMiDTO2.setId(2L);
        assertThat(nextPaymentMiDTO1).isNotEqualTo(nextPaymentMiDTO2);
        nextPaymentMiDTO1.setId(null);
        assertThat(nextPaymentMiDTO1).isNotEqualTo(nextPaymentMiDTO2);
    }
}
