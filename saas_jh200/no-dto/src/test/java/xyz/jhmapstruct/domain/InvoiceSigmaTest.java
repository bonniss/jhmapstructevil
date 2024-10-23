package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceSigmaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceSigma.class);
        InvoiceSigma invoiceSigma1 = getInvoiceSigmaSample1();
        InvoiceSigma invoiceSigma2 = new InvoiceSigma();
        assertThat(invoiceSigma1).isNotEqualTo(invoiceSigma2);

        invoiceSigma2.setId(invoiceSigma1.getId());
        assertThat(invoiceSigma1).isEqualTo(invoiceSigma2);

        invoiceSigma2 = getInvoiceSigmaSample2();
        assertThat(invoiceSigma1).isNotEqualTo(invoiceSigma2);
    }

    @Test
    void tenantTest() {
        InvoiceSigma invoiceSigma = getInvoiceSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        invoiceSigma.setTenant(masterTenantBack);
        assertThat(invoiceSigma.getTenant()).isEqualTo(masterTenantBack);

        invoiceSigma.tenant(null);
        assertThat(invoiceSigma.getTenant()).isNull();
    }
}
