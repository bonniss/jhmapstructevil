package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceVi.class);
        InvoiceVi invoiceVi1 = getInvoiceViSample1();
        InvoiceVi invoiceVi2 = new InvoiceVi();
        assertThat(invoiceVi1).isNotEqualTo(invoiceVi2);

        invoiceVi2.setId(invoiceVi1.getId());
        assertThat(invoiceVi1).isEqualTo(invoiceVi2);

        invoiceVi2 = getInvoiceViSample2();
        assertThat(invoiceVi1).isNotEqualTo(invoiceVi2);
    }
}
