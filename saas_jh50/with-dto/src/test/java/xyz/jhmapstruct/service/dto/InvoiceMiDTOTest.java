package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceMiDTO.class);
        InvoiceMiDTO invoiceMiDTO1 = new InvoiceMiDTO();
        invoiceMiDTO1.setId(1L);
        InvoiceMiDTO invoiceMiDTO2 = new InvoiceMiDTO();
        assertThat(invoiceMiDTO1).isNotEqualTo(invoiceMiDTO2);
        invoiceMiDTO2.setId(invoiceMiDTO1.getId());
        assertThat(invoiceMiDTO1).isEqualTo(invoiceMiDTO2);
        invoiceMiDTO2.setId(2L);
        assertThat(invoiceMiDTO1).isNotEqualTo(invoiceMiDTO2);
        invoiceMiDTO1.setId(null);
        assertThat(invoiceMiDTO1).isNotEqualTo(invoiceMiDTO2);
    }
}
