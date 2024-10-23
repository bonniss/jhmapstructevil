package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceViDTO.class);
        NextInvoiceViDTO nextInvoiceViDTO1 = new NextInvoiceViDTO();
        nextInvoiceViDTO1.setId(1L);
        NextInvoiceViDTO nextInvoiceViDTO2 = new NextInvoiceViDTO();
        assertThat(nextInvoiceViDTO1).isNotEqualTo(nextInvoiceViDTO2);
        nextInvoiceViDTO2.setId(nextInvoiceViDTO1.getId());
        assertThat(nextInvoiceViDTO1).isEqualTo(nextInvoiceViDTO2);
        nextInvoiceViDTO2.setId(2L);
        assertThat(nextInvoiceViDTO1).isNotEqualTo(nextInvoiceViDTO2);
        nextInvoiceViDTO1.setId(null);
        assertThat(nextInvoiceViDTO1).isNotEqualTo(nextInvoiceViDTO2);
    }
}
