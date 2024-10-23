package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentDTO.class);
        NextPaymentDTO nextPaymentDTO1 = new NextPaymentDTO();
        nextPaymentDTO1.setId(1L);
        NextPaymentDTO nextPaymentDTO2 = new NextPaymentDTO();
        assertThat(nextPaymentDTO1).isNotEqualTo(nextPaymentDTO2);
        nextPaymentDTO2.setId(nextPaymentDTO1.getId());
        assertThat(nextPaymentDTO1).isEqualTo(nextPaymentDTO2);
        nextPaymentDTO2.setId(2L);
        assertThat(nextPaymentDTO1).isNotEqualTo(nextPaymentDTO2);
        nextPaymentDTO1.setId(null);
        assertThat(nextPaymentDTO1).isNotEqualTo(nextPaymentDTO2);
    }
}
