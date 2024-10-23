package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceBetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceBeta.class);
        InvoiceBeta invoiceBeta1 = getInvoiceBetaSample1();
        InvoiceBeta invoiceBeta2 = new InvoiceBeta();
        assertThat(invoiceBeta1).isNotEqualTo(invoiceBeta2);

        invoiceBeta2.setId(invoiceBeta1.getId());
        assertThat(invoiceBeta1).isEqualTo(invoiceBeta2);

        invoiceBeta2 = getInvoiceBetaSample2();
        assertThat(invoiceBeta1).isNotEqualTo(invoiceBeta2);
    }

    @Test
    void tenantTest() {
        InvoiceBeta invoiceBeta = getInvoiceBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        invoiceBeta.setTenant(masterTenantBack);
        assertThat(invoiceBeta.getTenant()).isEqualTo(masterTenantBack);

        invoiceBeta.tenant(null);
        assertThat(invoiceBeta.getTenant()).isNull();
    }
}
