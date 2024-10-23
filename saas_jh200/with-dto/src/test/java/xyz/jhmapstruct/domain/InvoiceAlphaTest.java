package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceAlphaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceAlpha.class);
        InvoiceAlpha invoiceAlpha1 = getInvoiceAlphaSample1();
        InvoiceAlpha invoiceAlpha2 = new InvoiceAlpha();
        assertThat(invoiceAlpha1).isNotEqualTo(invoiceAlpha2);

        invoiceAlpha2.setId(invoiceAlpha1.getId());
        assertThat(invoiceAlpha1).isEqualTo(invoiceAlpha2);

        invoiceAlpha2 = getInvoiceAlphaSample2();
        assertThat(invoiceAlpha1).isNotEqualTo(invoiceAlpha2);
    }

    @Test
    void tenantTest() {
        InvoiceAlpha invoiceAlpha = getInvoiceAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        invoiceAlpha.setTenant(masterTenantBack);
        assertThat(invoiceAlpha.getTenant()).isEqualTo(masterTenantBack);

        invoiceAlpha.tenant(null);
        assertThat(invoiceAlpha.getTenant()).isNull();
    }
}
