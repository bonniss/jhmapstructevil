package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceMi.class);
        NextInvoiceMi nextInvoiceMi1 = getNextInvoiceMiSample1();
        NextInvoiceMi nextInvoiceMi2 = new NextInvoiceMi();
        assertThat(nextInvoiceMi1).isNotEqualTo(nextInvoiceMi2);

        nextInvoiceMi2.setId(nextInvoiceMi1.getId());
        assertThat(nextInvoiceMi1).isEqualTo(nextInvoiceMi2);

        nextInvoiceMi2 = getNextInvoiceMiSample2();
        assertThat(nextInvoiceMi1).isNotEqualTo(nextInvoiceMi2);
    }

    @Test
    void tenantTest() {
        NextInvoiceMi nextInvoiceMi = getNextInvoiceMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceMi.setTenant(masterTenantBack);
        assertThat(nextInvoiceMi.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceMi.tenant(null);
        assertThat(nextInvoiceMi.getTenant()).isNull();
    }
}
