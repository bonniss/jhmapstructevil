package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPowerShellDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPowerShellDTO.class);
        AlPowerShellDTO alPowerShellDTO1 = new AlPowerShellDTO();
        alPowerShellDTO1.setId(1L);
        AlPowerShellDTO alPowerShellDTO2 = new AlPowerShellDTO();
        assertThat(alPowerShellDTO1).isNotEqualTo(alPowerShellDTO2);
        alPowerShellDTO2.setId(alPowerShellDTO1.getId());
        assertThat(alPowerShellDTO1).isEqualTo(alPowerShellDTO2);
        alPowerShellDTO2.setId(2L);
        assertThat(alPowerShellDTO1).isNotEqualTo(alPowerShellDTO2);
        alPowerShellDTO1.setId(null);
        assertThat(alPowerShellDTO1).isNotEqualTo(alPowerShellDTO2);
    }
}
