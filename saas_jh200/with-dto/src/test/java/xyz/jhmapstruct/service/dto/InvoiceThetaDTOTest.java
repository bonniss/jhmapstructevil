package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceThetaDTO.class);
        InvoiceThetaDTO invoiceThetaDTO1 = new InvoiceThetaDTO();
        invoiceThetaDTO1.setId(1L);
        InvoiceThetaDTO invoiceThetaDTO2 = new InvoiceThetaDTO();
        assertThat(invoiceThetaDTO1).isNotEqualTo(invoiceThetaDTO2);
        invoiceThetaDTO2.setId(invoiceThetaDTO1.getId());
        assertThat(invoiceThetaDTO1).isEqualTo(invoiceThetaDTO2);
        invoiceThetaDTO2.setId(2L);
        assertThat(invoiceThetaDTO1).isNotEqualTo(invoiceThetaDTO2);
        invoiceThetaDTO1.setId(null);
        assertThat(invoiceThetaDTO1).isNotEqualTo(invoiceThetaDTO2);
    }
}
