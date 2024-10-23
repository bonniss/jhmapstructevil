package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceDTO.class);
        NextInvoiceDTO nextInvoiceDTO1 = new NextInvoiceDTO();
        nextInvoiceDTO1.setId(1L);
        NextInvoiceDTO nextInvoiceDTO2 = new NextInvoiceDTO();
        assertThat(nextInvoiceDTO1).isNotEqualTo(nextInvoiceDTO2);
        nextInvoiceDTO2.setId(nextInvoiceDTO1.getId());
        assertThat(nextInvoiceDTO1).isEqualTo(nextInvoiceDTO2);
        nextInvoiceDTO2.setId(2L);
        assertThat(nextInvoiceDTO1).isNotEqualTo(nextInvoiceDTO2);
        nextInvoiceDTO1.setId(null);
        assertThat(nextInvoiceDTO1).isNotEqualTo(nextInvoiceDTO2);
    }
}
