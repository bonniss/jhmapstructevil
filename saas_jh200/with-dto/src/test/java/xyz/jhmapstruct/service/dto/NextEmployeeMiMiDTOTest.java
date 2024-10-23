package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeMiMiDTO.class);
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO1 = new NextEmployeeMiMiDTO();
        nextEmployeeMiMiDTO1.setId(1L);
        NextEmployeeMiMiDTO nextEmployeeMiMiDTO2 = new NextEmployeeMiMiDTO();
        assertThat(nextEmployeeMiMiDTO1).isNotEqualTo(nextEmployeeMiMiDTO2);
        nextEmployeeMiMiDTO2.setId(nextEmployeeMiMiDTO1.getId());
        assertThat(nextEmployeeMiMiDTO1).isEqualTo(nextEmployeeMiMiDTO2);
        nextEmployeeMiMiDTO2.setId(2L);
        assertThat(nextEmployeeMiMiDTO1).isNotEqualTo(nextEmployeeMiMiDTO2);
        nextEmployeeMiMiDTO1.setId(null);
        assertThat(nextEmployeeMiMiDTO1).isNotEqualTo(nextEmployeeMiMiDTO2);
    }
}
