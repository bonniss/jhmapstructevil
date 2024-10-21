package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceMiMiDTO.class);
        InvoiceMiMiDTO invoiceMiMiDTO1 = new InvoiceMiMiDTO();
        invoiceMiMiDTO1.setId(1L);
        InvoiceMiMiDTO invoiceMiMiDTO2 = new InvoiceMiMiDTO();
        assertThat(invoiceMiMiDTO1).isNotEqualTo(invoiceMiMiDTO2);
        invoiceMiMiDTO2.setId(invoiceMiMiDTO1.getId());
        assertThat(invoiceMiMiDTO1).isEqualTo(invoiceMiMiDTO2);
        invoiceMiMiDTO2.setId(2L);
        assertThat(invoiceMiMiDTO1).isNotEqualTo(invoiceMiMiDTO2);
        invoiceMiMiDTO1.setId(null);
        assertThat(invoiceMiMiDTO1).isNotEqualTo(invoiceMiMiDTO2);
    }
}
