package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeAlphaDTO.class);
        NextEmployeeAlphaDTO nextEmployeeAlphaDTO1 = new NextEmployeeAlphaDTO();
        nextEmployeeAlphaDTO1.setId(1L);
        NextEmployeeAlphaDTO nextEmployeeAlphaDTO2 = new NextEmployeeAlphaDTO();
        assertThat(nextEmployeeAlphaDTO1).isNotEqualTo(nextEmployeeAlphaDTO2);
        nextEmployeeAlphaDTO2.setId(nextEmployeeAlphaDTO1.getId());
        assertThat(nextEmployeeAlphaDTO1).isEqualTo(nextEmployeeAlphaDTO2);
        nextEmployeeAlphaDTO2.setId(2L);
        assertThat(nextEmployeeAlphaDTO1).isNotEqualTo(nextEmployeeAlphaDTO2);
        nextEmployeeAlphaDTO1.setId(null);
        assertThat(nextEmployeeAlphaDTO1).isNotEqualTo(nextEmployeeAlphaDTO2);
    }
}
