package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceThetaTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceTheta.class);
        InvoiceTheta invoiceTheta1 = getInvoiceThetaSample1();
        InvoiceTheta invoiceTheta2 = new InvoiceTheta();
        assertThat(invoiceTheta1).isNotEqualTo(invoiceTheta2);

        invoiceTheta2.setId(invoiceTheta1.getId());
        assertThat(invoiceTheta1).isEqualTo(invoiceTheta2);

        invoiceTheta2 = getInvoiceThetaSample2();
        assertThat(invoiceTheta1).isNotEqualTo(invoiceTheta2);
    }

    @Test
    void tenantTest() {
        InvoiceTheta invoiceTheta = getInvoiceThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        invoiceTheta.setTenant(masterTenantBack);
        assertThat(invoiceTheta.getTenant()).isEqualTo(masterTenantBack);

        invoiceTheta.tenant(null);
        assertThat(invoiceTheta.getTenant()).isNull();
    }
}
