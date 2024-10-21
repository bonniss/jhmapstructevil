package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPowerShellViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPowerShellViDTO.class);
        AlPowerShellViDTO alPowerShellViDTO1 = new AlPowerShellViDTO();
        alPowerShellViDTO1.setId(1L);
        AlPowerShellViDTO alPowerShellViDTO2 = new AlPowerShellViDTO();
        assertThat(alPowerShellViDTO1).isNotEqualTo(alPowerShellViDTO2);
        alPowerShellViDTO2.setId(alPowerShellViDTO1.getId());
        assertThat(alPowerShellViDTO1).isEqualTo(alPowerShellViDTO2);
        alPowerShellViDTO2.setId(2L);
        assertThat(alPowerShellViDTO1).isNotEqualTo(alPowerShellViDTO2);
        alPowerShellViDTO1.setId(null);
        assertThat(alPowerShellViDTO1).isNotEqualTo(alPowerShellViDTO2);
    }
}
