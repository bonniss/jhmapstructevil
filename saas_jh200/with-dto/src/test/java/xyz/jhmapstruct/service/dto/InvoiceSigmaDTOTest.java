package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceSigmaDTO.class);
        InvoiceSigmaDTO invoiceSigmaDTO1 = new InvoiceSigmaDTO();
        invoiceSigmaDTO1.setId(1L);
        InvoiceSigmaDTO invoiceSigmaDTO2 = new InvoiceSigmaDTO();
        assertThat(invoiceSigmaDTO1).isNotEqualTo(invoiceSigmaDTO2);
        invoiceSigmaDTO2.setId(invoiceSigmaDTO1.getId());
        assertThat(invoiceSigmaDTO1).isEqualTo(invoiceSigmaDTO2);
        invoiceSigmaDTO2.setId(2L);
        assertThat(invoiceSigmaDTO1).isNotEqualTo(invoiceSigmaDTO2);
        invoiceSigmaDTO1.setId(null);
        assertThat(invoiceSigmaDTO1).isNotEqualTo(invoiceSigmaDTO2);
    }
}
