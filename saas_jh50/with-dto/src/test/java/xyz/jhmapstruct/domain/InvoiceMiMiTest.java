package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceMiMiTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceMiMi.class);
        InvoiceMiMi invoiceMiMi1 = getInvoiceMiMiSample1();
        InvoiceMiMi invoiceMiMi2 = new InvoiceMiMi();
        assertThat(invoiceMiMi1).isNotEqualTo(invoiceMiMi2);

        invoiceMiMi2.setId(invoiceMiMi1.getId());
        assertThat(invoiceMiMi1).isEqualTo(invoiceMiMi2);

        invoiceMiMi2 = getInvoiceMiMiSample2();
        assertThat(invoiceMiMi1).isNotEqualTo(invoiceMiMi2);
    }

    @Test
    void tenantTest() {
        InvoiceMiMi invoiceMiMi = getInvoiceMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        invoiceMiMi.setTenant(masterTenantBack);
        assertThat(invoiceMiMi.getTenant()).isEqualTo(masterTenantBack);

        invoiceMiMi.tenant(null);
        assertThat(invoiceMiMi.getTenant()).isNull();
    }
}
