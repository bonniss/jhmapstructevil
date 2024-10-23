package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceBetaDTO.class);
        NextInvoiceBetaDTO nextInvoiceBetaDTO1 = new NextInvoiceBetaDTO();
        nextInvoiceBetaDTO1.setId(1L);
        NextInvoiceBetaDTO nextInvoiceBetaDTO2 = new NextInvoiceBetaDTO();
        assertThat(nextInvoiceBetaDTO1).isNotEqualTo(nextInvoiceBetaDTO2);
        nextInvoiceBetaDTO2.setId(nextInvoiceBetaDTO1.getId());
        assertThat(nextInvoiceBetaDTO1).isEqualTo(nextInvoiceBetaDTO2);
        nextInvoiceBetaDTO2.setId(2L);
        assertThat(nextInvoiceBetaDTO1).isNotEqualTo(nextInvoiceBetaDTO2);
        nextInvoiceBetaDTO1.setId(null);
        assertThat(nextInvoiceBetaDTO1).isNotEqualTo(nextInvoiceBetaDTO2);
    }
}
