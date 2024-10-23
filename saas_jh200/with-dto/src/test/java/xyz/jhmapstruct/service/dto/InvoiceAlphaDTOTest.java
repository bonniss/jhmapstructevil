package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceAlphaDTO.class);
        InvoiceAlphaDTO invoiceAlphaDTO1 = new InvoiceAlphaDTO();
        invoiceAlphaDTO1.setId(1L);
        InvoiceAlphaDTO invoiceAlphaDTO2 = new InvoiceAlphaDTO();
        assertThat(invoiceAlphaDTO1).isNotEqualTo(invoiceAlphaDTO2);
        invoiceAlphaDTO2.setId(invoiceAlphaDTO1.getId());
        assertThat(invoiceAlphaDTO1).isEqualTo(invoiceAlphaDTO2);
        invoiceAlphaDTO2.setId(2L);
        assertThat(invoiceAlphaDTO1).isNotEqualTo(invoiceAlphaDTO2);
        invoiceAlphaDTO1.setId(null);
        assertThat(invoiceAlphaDTO1).isNotEqualTo(invoiceAlphaDTO2);
    }
}
