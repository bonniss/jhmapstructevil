package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceGammaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceGamma.class);
        InvoiceGamma invoiceGamma1 = getInvoiceGammaSample1();
        InvoiceGamma invoiceGamma2 = new InvoiceGamma();
        assertThat(invoiceGamma1).isNotEqualTo(invoiceGamma2);

        invoiceGamma2.setId(invoiceGamma1.getId());
        assertThat(invoiceGamma1).isEqualTo(invoiceGamma2);

        invoiceGamma2 = getInvoiceGammaSample2();
        assertThat(invoiceGamma1).isNotEqualTo(invoiceGamma2);
    }

    @Test
    void tenantTest() {
        InvoiceGamma invoiceGamma = getInvoiceGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        invoiceGamma.setTenant(masterTenantBack);
        assertThat(invoiceGamma.getTenant()).isEqualTo(masterTenantBack);

        invoiceGamma.tenant(null);
        assertThat(invoiceGamma.getTenant()).isNull();
    }
}
