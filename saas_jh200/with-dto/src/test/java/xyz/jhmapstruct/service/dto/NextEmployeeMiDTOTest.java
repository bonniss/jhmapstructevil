package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeMiDTO.class);
        NextEmployeeMiDTO nextEmployeeMiDTO1 = new NextEmployeeMiDTO();
        nextEmployeeMiDTO1.setId(1L);
        NextEmployeeMiDTO nextEmployeeMiDTO2 = new NextEmployeeMiDTO();
        assertThat(nextEmployeeMiDTO1).isNotEqualTo(nextEmployeeMiDTO2);
        nextEmployeeMiDTO2.setId(nextEmployeeMiDTO1.getId());
        assertThat(nextEmployeeMiDTO1).isEqualTo(nextEmployeeMiDTO2);
        nextEmployeeMiDTO2.setId(2L);
        assertThat(nextEmployeeMiDTO1).isNotEqualTo(nextEmployeeMiDTO2);
        nextEmployeeMiDTO1.setId(null);
        assertThat(nextEmployeeMiDTO1).isNotEqualTo(nextEmployeeMiDTO2);
    }
}
