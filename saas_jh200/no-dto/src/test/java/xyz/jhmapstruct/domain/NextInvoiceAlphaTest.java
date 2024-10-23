package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceAlphaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceAlphaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceAlpha.class);
        NextInvoiceAlpha nextInvoiceAlpha1 = getNextInvoiceAlphaSample1();
        NextInvoiceAlpha nextInvoiceAlpha2 = new NextInvoiceAlpha();
        assertThat(nextInvoiceAlpha1).isNotEqualTo(nextInvoiceAlpha2);

        nextInvoiceAlpha2.setId(nextInvoiceAlpha1.getId());
        assertThat(nextInvoiceAlpha1).isEqualTo(nextInvoiceAlpha2);

        nextInvoiceAlpha2 = getNextInvoiceAlphaSample2();
        assertThat(nextInvoiceAlpha1).isNotEqualTo(nextInvoiceAlpha2);
    }

    @Test
    void tenantTest() {
        NextInvoiceAlpha nextInvoiceAlpha = getNextInvoiceAlphaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceAlpha.setTenant(masterTenantBack);
        assertThat(nextInvoiceAlpha.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceAlpha.tenant(null);
        assertThat(nextInvoiceAlpha.getTenant()).isNull();
    }
}
