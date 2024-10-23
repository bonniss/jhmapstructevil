package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentViDTO.class);
        NextPaymentViDTO nextPaymentViDTO1 = new NextPaymentViDTO();
        nextPaymentViDTO1.setId(1L);
        NextPaymentViDTO nextPaymentViDTO2 = new NextPaymentViDTO();
        assertThat(nextPaymentViDTO1).isNotEqualTo(nextPaymentViDTO2);
        nextPaymentViDTO2.setId(nextPaymentViDTO1.getId());
        assertThat(nextPaymentViDTO1).isEqualTo(nextPaymentViDTO2);
        nextPaymentViDTO2.setId(2L);
        assertThat(nextPaymentViDTO1).isNotEqualTo(nextPaymentViDTO2);
        nextPaymentViDTO1.setId(null);
        assertThat(nextPaymentViDTO1).isNotEqualTo(nextPaymentViDTO2);
    }
}
