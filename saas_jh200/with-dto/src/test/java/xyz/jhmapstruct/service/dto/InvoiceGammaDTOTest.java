package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceGammaDTO.class);
        InvoiceGammaDTO invoiceGammaDTO1 = new InvoiceGammaDTO();
        invoiceGammaDTO1.setId(1L);
        InvoiceGammaDTO invoiceGammaDTO2 = new InvoiceGammaDTO();
        assertThat(invoiceGammaDTO1).isNotEqualTo(invoiceGammaDTO2);
        invoiceGammaDTO2.setId(invoiceGammaDTO1.getId());
        assertThat(invoiceGammaDTO1).isEqualTo(invoiceGammaDTO2);
        invoiceGammaDTO2.setId(2L);
        assertThat(invoiceGammaDTO1).isNotEqualTo(invoiceGammaDTO2);
        invoiceGammaDTO1.setId(null);
        assertThat(invoiceGammaDTO1).isNotEqualTo(invoiceGammaDTO2);
    }
}
