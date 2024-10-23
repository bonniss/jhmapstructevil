package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceAlphaDTO.class);
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO1 = new NextInvoiceAlphaDTO();
        nextInvoiceAlphaDTO1.setId(1L);
        NextInvoiceAlphaDTO nextInvoiceAlphaDTO2 = new NextInvoiceAlphaDTO();
        assertThat(nextInvoiceAlphaDTO1).isNotEqualTo(nextInvoiceAlphaDTO2);
        nextInvoiceAlphaDTO2.setId(nextInvoiceAlphaDTO1.getId());
        assertThat(nextInvoiceAlphaDTO1).isEqualTo(nextInvoiceAlphaDTO2);
        nextInvoiceAlphaDTO2.setId(2L);
        assertThat(nextInvoiceAlphaDTO1).isNotEqualTo(nextInvoiceAlphaDTO2);
        nextInvoiceAlphaDTO1.setId(null);
        assertThat(nextInvoiceAlphaDTO1).isNotEqualTo(nextInvoiceAlphaDTO2);
    }
}
