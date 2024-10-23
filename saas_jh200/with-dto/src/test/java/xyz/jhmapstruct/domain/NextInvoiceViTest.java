package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceVi.class);
        NextInvoiceVi nextInvoiceVi1 = getNextInvoiceViSample1();
        NextInvoiceVi nextInvoiceVi2 = new NextInvoiceVi();
        assertThat(nextInvoiceVi1).isNotEqualTo(nextInvoiceVi2);

        nextInvoiceVi2.setId(nextInvoiceVi1.getId());
        assertThat(nextInvoiceVi1).isEqualTo(nextInvoiceVi2);

        nextInvoiceVi2 = getNextInvoiceViSample2();
        assertThat(nextInvoiceVi1).isNotEqualTo(nextInvoiceVi2);
    }

    @Test
    void tenantTest() {
        NextInvoiceVi nextInvoiceVi = getNextInvoiceViRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceVi.setTenant(masterTenantBack);
        assertThat(nextInvoiceVi.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceVi.tenant(null);
        assertThat(nextInvoiceVi.getTenant()).isNull();
    }
}
