package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceThetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceThetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceTheta.class);
        NextInvoiceTheta nextInvoiceTheta1 = getNextInvoiceThetaSample1();
        NextInvoiceTheta nextInvoiceTheta2 = new NextInvoiceTheta();
        assertThat(nextInvoiceTheta1).isNotEqualTo(nextInvoiceTheta2);

        nextInvoiceTheta2.setId(nextInvoiceTheta1.getId());
        assertThat(nextInvoiceTheta1).isEqualTo(nextInvoiceTheta2);

        nextInvoiceTheta2 = getNextInvoiceThetaSample2();
        assertThat(nextInvoiceTheta1).isNotEqualTo(nextInvoiceTheta2);
    }

    @Test
    void tenantTest() {
        NextInvoiceTheta nextInvoiceTheta = getNextInvoiceThetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceTheta.setTenant(masterTenantBack);
        assertThat(nextInvoiceTheta.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceTheta.tenant(null);
        assertThat(nextInvoiceTheta.getTenant()).isNull();
    }
}
