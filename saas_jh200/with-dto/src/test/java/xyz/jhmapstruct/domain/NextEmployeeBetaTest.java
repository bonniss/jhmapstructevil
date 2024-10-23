package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;
import static xyz.jhmapstruct.domain.NextEmployeeBetaTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeBetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeBeta.class);
        NextEmployeeBeta nextEmployeeBeta1 = getNextEmployeeBetaSample1();
        NextEmployeeBeta nextEmployeeBeta2 = new NextEmployeeBeta();
        assertThat(nextEmployeeBeta1).isNotEqualTo(nextEmployeeBeta2);

        nextEmployeeBeta2.setId(nextEmployeeBeta1.getId());
        assertThat(nextEmployeeBeta1).isEqualTo(nextEmployeeBeta2);

        nextEmployeeBeta2 = getNextEmployeeBetaSample2();
        assertThat(nextEmployeeBeta1).isNotEqualTo(nextEmployeeBeta2);
    }

    @Test
    void tenantTest() {
        NextEmployeeBeta nextEmployeeBeta = getNextEmployeeBetaRandomSampleGenerator();
        MasterTenant masterTenantBack = getMasterTenantRandomSampleGenerator();

        nextEmployeeBeta.setTenant(masterTenantBack);
        assertThat(nextEmployeeBeta.getTenant()).isEqualTo(masterTenantBack);

        nextEmployeeBeta.tenant(null);
        assertThat(nextEmployeeBeta.getTenant()).isNull();
    }
}
