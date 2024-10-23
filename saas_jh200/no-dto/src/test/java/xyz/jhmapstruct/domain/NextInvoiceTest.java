package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoice.class);
        NextInvoice nextInvoice1 = getNextInvoiceSample1();
        NextInvoice nextInvoice2 = new NextInvoice();
        assertThat(nextInvoice1).isNotEqualTo(nextInvoice2);

        nextInvoice2.setId(nextInvoice1.getId());
        assertThat(nextInvoice1).isEqualTo(nextInvoice2);

        nextInvoice2 = getNextInvoiceSample2();
        assertThat(nextInvoice1).isNotEqualTo(nextInvoice2);
    }

    @Test
    void tenantTest() {
        NextInvoice nextInvoice = getNextInvoiceRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoice.setTenant(masterTenantBack);
        assertThat(nextInvoice.getTenant()).isEqualTo(masterTenantBack);

        nextInvoice.tenant(null);
        assertThat(nextInvoice.getTenant()).isNull();
    }
}
