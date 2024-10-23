package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceGammaDTO.class);
        NextInvoiceGammaDTO nextInvoiceGammaDTO1 = new NextInvoiceGammaDTO();
        nextInvoiceGammaDTO1.setId(1L);
        NextInvoiceGammaDTO nextInvoiceGammaDTO2 = new NextInvoiceGammaDTO();
        assertThat(nextInvoiceGammaDTO1).isNotEqualTo(nextInvoiceGammaDTO2);
        nextInvoiceGammaDTO2.setId(nextInvoiceGammaDTO1.getId());
        assertThat(nextInvoiceGammaDTO1).isEqualTo(nextInvoiceGammaDTO2);
        nextInvoiceGammaDTO2.setId(2L);
        assertThat(nextInvoiceGammaDTO1).isNotEqualTo(nextInvoiceGammaDTO2);
        nextInvoiceGammaDTO1.setId(null);
        assertThat(nextInvoiceGammaDTO1).isNotEqualTo(nextInvoiceGammaDTO2);
    }
}
