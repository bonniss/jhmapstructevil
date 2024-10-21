package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceViDTO.class);
        InvoiceViDTO invoiceViDTO1 = new InvoiceViDTO();
        invoiceViDTO1.setId(1L);
        InvoiceViDTO invoiceViDTO2 = new InvoiceViDTO();
        assertThat(invoiceViDTO1).isNotEqualTo(invoiceViDTO2);
        invoiceViDTO2.setId(invoiceViDTO1.getId());
        assertThat(invoiceViDTO1).isEqualTo(invoiceViDTO2);
        invoiceViDTO2.setId(2L);
        assertThat(invoiceViDTO1).isNotEqualTo(invoiceViDTO2);
        invoiceViDTO1.setId(null);
        assertThat(invoiceViDTO1).isNotEqualTo(invoiceViDTO2);
    }
}
