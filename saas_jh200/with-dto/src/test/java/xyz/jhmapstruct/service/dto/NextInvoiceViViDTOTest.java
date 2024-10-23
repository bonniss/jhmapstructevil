package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceViViDTO.class);
        NextInvoiceViViDTO nextInvoiceViViDTO1 = new NextInvoiceViViDTO();
        nextInvoiceViViDTO1.setId(1L);
        NextInvoiceViViDTO nextInvoiceViViDTO2 = new NextInvoiceViViDTO();
        assertThat(nextInvoiceViViDTO1).isNotEqualTo(nextInvoiceViViDTO2);
        nextInvoiceViViDTO2.setId(nextInvoiceViViDTO1.getId());
        assertThat(nextInvoiceViViDTO1).isEqualTo(nextInvoiceViViDTO2);
        nextInvoiceViViDTO2.setId(2L);
        assertThat(nextInvoiceViViDTO1).isNotEqualTo(nextInvoiceViViDTO2);
        nextInvoiceViViDTO1.setId(null);
        assertThat(nextInvoiceViViDTO1).isNotEqualTo(nextInvoiceViViDTO2);
    }
}
