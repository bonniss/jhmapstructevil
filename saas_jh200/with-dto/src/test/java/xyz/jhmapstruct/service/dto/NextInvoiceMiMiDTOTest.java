package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceMiMiDTO.class);
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO1 = new NextInvoiceMiMiDTO();
        nextInvoiceMiMiDTO1.setId(1L);
        NextInvoiceMiMiDTO nextInvoiceMiMiDTO2 = new NextInvoiceMiMiDTO();
        assertThat(nextInvoiceMiMiDTO1).isNotEqualTo(nextInvoiceMiMiDTO2);
        nextInvoiceMiMiDTO2.setId(nextInvoiceMiMiDTO1.getId());
        assertThat(nextInvoiceMiMiDTO1).isEqualTo(nextInvoiceMiMiDTO2);
        nextInvoiceMiMiDTO2.setId(2L);
        assertThat(nextInvoiceMiMiDTO1).isNotEqualTo(nextInvoiceMiMiDTO2);
        nextInvoiceMiMiDTO1.setId(null);
        assertThat(nextInvoiceMiMiDTO1).isNotEqualTo(nextInvoiceMiMiDTO2);
    }
}
