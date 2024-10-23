package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceBetaDTO.class);
        InvoiceBetaDTO invoiceBetaDTO1 = new InvoiceBetaDTO();
        invoiceBetaDTO1.setId(1L);
        InvoiceBetaDTO invoiceBetaDTO2 = new InvoiceBetaDTO();
        assertThat(invoiceBetaDTO1).isNotEqualTo(invoiceBetaDTO2);
        invoiceBetaDTO2.setId(invoiceBetaDTO1.getId());
        assertThat(invoiceBetaDTO1).isEqualTo(invoiceBetaDTO2);
        invoiceBetaDTO2.setId(2L);
        assertThat(invoiceBetaDTO1).isNotEqualTo(invoiceBetaDTO2);
        invoiceBetaDTO1.setId(null);
        assertThat(invoiceBetaDTO1).isNotEqualTo(invoiceBetaDTO2);
    }
}
