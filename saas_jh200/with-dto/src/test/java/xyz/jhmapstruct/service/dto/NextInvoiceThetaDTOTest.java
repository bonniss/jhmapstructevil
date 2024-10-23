package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceThetaDTO.class);
        NextInvoiceThetaDTO nextInvoiceThetaDTO1 = new NextInvoiceThetaDTO();
        nextInvoiceThetaDTO1.setId(1L);
        NextInvoiceThetaDTO nextInvoiceThetaDTO2 = new NextInvoiceThetaDTO();
        assertThat(nextInvoiceThetaDTO1).isNotEqualTo(nextInvoiceThetaDTO2);
        nextInvoiceThetaDTO2.setId(nextInvoiceThetaDTO1.getId());
        assertThat(nextInvoiceThetaDTO1).isEqualTo(nextInvoiceThetaDTO2);
        nextInvoiceThetaDTO2.setId(2L);
        assertThat(nextInvoiceThetaDTO1).isNotEqualTo(nextInvoiceThetaDTO2);
        nextInvoiceThetaDTO1.setId(null);
        assertThat(nextInvoiceThetaDTO1).isNotEqualTo(nextInvoiceThetaDTO2);
    }
}
