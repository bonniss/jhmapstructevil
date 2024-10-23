package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceSigmaDTO.class);
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO1 = new NextInvoiceSigmaDTO();
        nextInvoiceSigmaDTO1.setId(1L);
        NextInvoiceSigmaDTO nextInvoiceSigmaDTO2 = new NextInvoiceSigmaDTO();
        assertThat(nextInvoiceSigmaDTO1).isNotEqualTo(nextInvoiceSigmaDTO2);
        nextInvoiceSigmaDTO2.setId(nextInvoiceSigmaDTO1.getId());
        assertThat(nextInvoiceSigmaDTO1).isEqualTo(nextInvoiceSigmaDTO2);
        nextInvoiceSigmaDTO2.setId(2L);
        assertThat(nextInvoiceSigmaDTO1).isNotEqualTo(nextInvoiceSigmaDTO2);
        nextInvoiceSigmaDTO1.setId(null);
        assertThat(nextInvoiceSigmaDTO1).isNotEqualTo(nextInvoiceSigmaDTO2);
    }
}
