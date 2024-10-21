package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceViVi.class);
        InvoiceViVi invoiceViVi1 = getInvoiceViViSample1();
        InvoiceViVi invoiceViVi2 = new InvoiceViVi();
        assertThat(invoiceViVi1).isNotEqualTo(invoiceViVi2);

        invoiceViVi2.setId(invoiceViVi1.getId());
        assertThat(invoiceViVi1).isEqualTo(invoiceViVi2);

        invoiceViVi2 = getInvoiceViViSample2();
        assertThat(invoiceViVi1).isNotEqualTo(invoiceViVi2);
    }
}
