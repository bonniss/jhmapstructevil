package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentViViDTO.class);
        NextPaymentViViDTO nextPaymentViViDTO1 = new NextPaymentViViDTO();
        nextPaymentViViDTO1.setId(1L);
        NextPaymentViViDTO nextPaymentViViDTO2 = new NextPaymentViViDTO();
        assertThat(nextPaymentViViDTO1).isNotEqualTo(nextPaymentViViDTO2);
        nextPaymentViViDTO2.setId(nextPaymentViViDTO1.getId());
        assertThat(nextPaymentViViDTO1).isEqualTo(nextPaymentViViDTO2);
        nextPaymentViViDTO2.setId(2L);
        assertThat(nextPaymentViViDTO1).isNotEqualTo(nextPaymentViViDTO2);
        nextPaymentViViDTO1.setId(null);
        assertThat(nextPaymentViViDTO1).isNotEqualTo(nextPaymentViViDTO2);
    }
}
