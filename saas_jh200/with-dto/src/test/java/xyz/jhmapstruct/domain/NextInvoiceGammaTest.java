package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceGammaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceGammaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceGamma.class);
        NextInvoiceGamma nextInvoiceGamma1 = getNextInvoiceGammaSample1();
        NextInvoiceGamma nextInvoiceGamma2 = new NextInvoiceGamma();
        assertThat(nextInvoiceGamma1).isNotEqualTo(nextInvoiceGamma2);

        nextInvoiceGamma2.setId(nextInvoiceGamma1.getId());
        assertThat(nextInvoiceGamma1).isEqualTo(nextInvoiceGamma2);

        nextInvoiceGamma2 = getNextInvoiceGammaSample2();
        assertThat(nextInvoiceGamma1).isNotEqualTo(nextInvoiceGamma2);
    }

    @Test
    void tenantTest() {
        NextInvoiceGamma nextInvoiceGamma = getNextInvoiceGammaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceGamma.setTenant(masterTenantBack);
        assertThat(nextInvoiceGamma.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceGamma.tenant(null);
        assertThat(nextInvoiceGamma.getTenant()).isNull();
    }
}
