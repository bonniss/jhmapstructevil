package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceBeta.class);
        NextInvoiceBeta nextInvoiceBeta1 = getNextInvoiceBetaSample1();
        NextInvoiceBeta nextInvoiceBeta2 = new NextInvoiceBeta();
        assertThat(nextInvoiceBeta1).isNotEqualTo(nextInvoiceBeta2);

        nextInvoiceBeta2.setId(nextInvoiceBeta1.getId());
        assertThat(nextInvoiceBeta1).isEqualTo(nextInvoiceBeta2);

        nextInvoiceBeta2 = getNextInvoiceBetaSample2();
        assertThat(nextInvoiceBeta1).isNotEqualTo(nextInvoiceBeta2);
    }

    @Test
    void tenantTest() {
        NextInvoiceBeta nextInvoiceBeta = getNextInvoiceBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceBeta.setTenant(masterTenantBack);
        assertThat(nextInvoiceBeta.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceBeta.tenant(null);
        assertThat(nextInvoiceBeta.getTenant()).isNull();
    }
}
