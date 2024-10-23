package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceMiMi.class);
        NextInvoiceMiMi nextInvoiceMiMi1 = getNextInvoiceMiMiSample1();
        NextInvoiceMiMi nextInvoiceMiMi2 = new NextInvoiceMiMi();
        assertThat(nextInvoiceMiMi1).isNotEqualTo(nextInvoiceMiMi2);

        nextInvoiceMiMi2.setId(nextInvoiceMiMi1.getId());
        assertThat(nextInvoiceMiMi1).isEqualTo(nextInvoiceMiMi2);

        nextInvoiceMiMi2 = getNextInvoiceMiMiSample2();
        assertThat(nextInvoiceMiMi1).isNotEqualTo(nextInvoiceMiMi2);
    }

    @Test
    void tenantTest() {
        NextInvoiceMiMi nextInvoiceMiMi = getNextInvoiceMiMiRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceMiMi.setTenant(masterTenantBack);
        assertThat(nextInvoiceMiMi.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceMiMi.tenant(null);
        assertThat(nextInvoiceMiMi.getTenant()).isNull();
    }
}
