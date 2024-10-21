package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class MasterTenantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterTenantDTO.class);
        MasterTenantDTO masterTenantDTO1 = new MasterTenantDTO();
        masterTenantDTO1.setId(1L);
        MasterTenantDTO masterTenantDTO2 = new MasterTenantDTO();
        assertThat(masterTenantDTO1).isNotEqualTo(masterTenantDTO2);
        masterTenantDTO2.setId(masterTenantDTO1.getId());
        assertThat(masterTenantDTO1).isEqualTo(masterTenantDTO2);
        masterTenantDTO2.setId(2L);
        assertThat(masterTenantDTO1).isNotEqualTo(masterTenantDTO2);
        masterTenantDTO1.setId(null);
        assertThat(masterTenantDTO1).isNotEqualTo(masterTenantDTO2);
    }
}
