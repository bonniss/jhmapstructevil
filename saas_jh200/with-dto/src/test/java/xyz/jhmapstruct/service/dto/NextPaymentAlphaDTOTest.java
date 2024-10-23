package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextPaymentAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextPaymentAlphaDTO.class);
        NextPaymentAlphaDTO nextPaymentAlphaDTO1 = new NextPaymentAlphaDTO();
        nextPaymentAlphaDTO1.setId(1L);
        NextPaymentAlphaDTO nextPaymentAlphaDTO2 = new NextPaymentAlphaDTO();
        assertThat(nextPaymentAlphaDTO1).isNotEqualTo(nextPaymentAlphaDTO2);
        nextPaymentAlphaDTO2.setId(nextPaymentAlphaDTO1.getId());
        assertThat(nextPaymentAlphaDTO1).isEqualTo(nextPaymentAlphaDTO2);
        nextPaymentAlphaDTO2.setId(2L);
        assertThat(nextPaymentAlphaDTO1).isNotEqualTo(nextPaymentAlphaDTO2);
        nextPaymentAlphaDTO1.setId(null);
        assertThat(nextPaymentAlphaDTO1).isNotEqualTo(nextPaymentAlphaDTO2);
    }
}
