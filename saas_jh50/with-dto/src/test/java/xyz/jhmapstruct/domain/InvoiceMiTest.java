package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.InvoiceMiTestSamples.*;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class InvoiceMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceMi.class);
        InvoiceMi invoiceMi1 = getInvoiceMiSample1();
        InvoiceMi invoiceMi2 = new InvoiceMi();
        assertThat(invoiceMi1).isNotEqualTo(invoiceMi2);

        invoiceMi2.setId(invoiceMi1.getId());
        assertThat(invoiceMi1).isEqualTo(invoiceMi2);

        invoiceMi2 = getInvoiceMiSample2();
        assertThat(invoiceMi1).isNotEqualTo(invoiceMi2);
    }

    @Test
    void tenantTest() {
        InvoiceMi invoiceMi = getInvoiceMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        invoiceMi.setTenant(masterTenantBack);
        assertThat(invoiceMi.getTenant()).isEqualTo(masterTenantBack);

        invoiceMi.tenant(null);
        assertThat(invoiceMi.getTenant()).isNull();
    }
}
