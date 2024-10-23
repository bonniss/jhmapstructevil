package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceMiDTO.class);
        NextInvoiceMiDTO nextInvoiceMiDTO1 = new NextInvoiceMiDTO();
        nextInvoiceMiDTO1.setId(1L);
        NextInvoiceMiDTO nextInvoiceMiDTO2 = new NextInvoiceMiDTO();
        assertThat(nextInvoiceMiDTO1).isNotEqualTo(nextInvoiceMiDTO2);
        nextInvoiceMiDTO2.setId(nextInvoiceMiDTO1.getId());
        assertThat(nextInvoiceMiDTO1).isEqualTo(nextInvoiceMiDTO2);
        nextInvoiceMiDTO2.setId(2L);
        assertThat(nextInvoiceMiDTO1).isNotEqualTo(nextInvoiceMiDTO2);
        nextInvoiceMiDTO1.setId(null);
        assertThat(nextInvoiceMiDTO1).isNotEqualTo(nextInvoiceMiDTO2);
    }
}
