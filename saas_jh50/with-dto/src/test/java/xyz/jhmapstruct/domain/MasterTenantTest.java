package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.MasterTenantTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class MasterTenantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterTenant.class);
        MasterTenant masterTenant1 = getMasterTenantSample1();
        MasterTenant masterTenant2 = new MasterTenant();
        assertThat(masterTenant1).isNotEqualTo(masterTenant2);

        masterTenant2.setId(masterTenant1.getId());
        assertThat(masterTenant1).isEqualTo(masterTenant2);

        masterTenant2 = getMasterTenantSample2();
        assertThat(masterTenant1).isNotEqualTo(masterTenant2);
    }
}
