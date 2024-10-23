package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextInvoiceSigmaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextInvoiceSigmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextInvoiceSigma.class);
        NextInvoiceSigma nextInvoiceSigma1 = getNextInvoiceSigmaSample1();
        NextInvoiceSigma nextInvoiceSigma2 = new NextInvoiceSigma();
        assertThat(nextInvoiceSigma1).isNotEqualTo(nextInvoiceSigma2);

        nextInvoiceSigma2.setId(nextInvoiceSigma1.getId());
        assertThat(nextInvoiceSigma1).isEqualTo(nextInvoiceSigma2);

        nextInvoiceSigma2 = getNextInvoiceSigmaSample2();
        assertThat(nextInvoiceSigma1).isNotEqualTo(nextInvoiceSigma2);
    }

    @Test
    void tenantTest() {
        NextInvoiceSigma nextInvoiceSigma = getNextInvoiceSigmaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextInvoiceSigma.setTenant(masterTenantBack);
        assertThat(nextInvoiceSigma.getTenant()).isEqualTo(masterTenantBack);

        nextInvoiceSigma.tenant(null);
        assertThat(nextInvoiceSigma.getTenant()).isNull();
    }
}
