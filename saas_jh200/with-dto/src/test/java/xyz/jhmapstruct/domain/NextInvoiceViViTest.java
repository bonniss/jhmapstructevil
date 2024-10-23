package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceViVi.class);
        NextInvoiceViVi nextInvoiceViVi1 = getNextInvoiceViViSample1();
        NextInvoiceViVi nextInvoiceViVi2 = new NextInvoiceViVi();
        assertThat(nextInvoiceViVi1).isNotEqualTo(nextInvoiceViVi2);

        nextInvoiceViVi2.setId(nextInvoiceViVi1.getId());
        assertThat(nextInvoiceViVi1).isEqualTo(nextInvoiceViVi2);

        nextInvoiceViVi2 = getNextInvoiceViViSample2();
        assertThat(nextInvoiceViVi1).isNotEqualTo(nextInvoiceViVi2);
    }

    @Test
    void tenantTest() {
        NextInvoiceViVi nextInvoiceViVi = getNextInvoiceViViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceViVi.setTenant(masterTenantBack);
        assertThat(nextInvoiceViVi.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceViVi.tenant(null);
        assertThat(nextInvoiceViVi.getTenant()).isNull();
    }
}
