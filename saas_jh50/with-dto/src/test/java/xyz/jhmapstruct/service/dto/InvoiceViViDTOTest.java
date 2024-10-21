package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceViViDTO.class);
        InvoiceViViDTO invoiceViViDTO1 = new InvoiceViViDTO();
        invoiceViViDTO1.setId(1L);
        InvoiceViViDTO invoiceViViDTO2 = new InvoiceViViDTO();
        assertThat(invoiceViViDTO1).isNotEqualTo(invoiceViViDTO2);
        invoiceViViDTO2.setId(invoiceViViDTO1.getId());
        assertThat(invoiceViViDTO1).isEqualTo(invoiceViViDTO2);
        invoiceViViDTO2.setId(2L);
        assertThat(invoiceViViDTO1).isNotEqualTo(invoiceViViDTO2);
        invoiceViViDTO1.setId(null);
        assertThat(invoiceViViDTO1).isNotEqualTo(invoiceViViDTO2);
    }
}
