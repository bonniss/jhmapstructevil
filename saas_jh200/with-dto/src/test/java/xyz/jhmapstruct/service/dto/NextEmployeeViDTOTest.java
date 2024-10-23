package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeViDTO.class);
        NextEmployeeViDTO nextEmployeeViDTO1 = new NextEmployeeViDTO();
        nextEmployeeViDTO1.setId(1L);
        NextEmployeeViDTO nextEmployeeViDTO2 = new NextEmployeeViDTO();
        assertThat(nextEmployeeViDTO1).isNotEqualTo(nextEmployeeViDTO2);
        nextEmployeeViDTO2.setId(nextEmployeeViDTO1.getId());
        assertThat(nextEmployeeViDTO1).isEqualTo(nextEmployeeViDTO2);
        nextEmployeeViDTO2.setId(2L);
        assertThat(nextEmployeeViDTO1).isNotEqualTo(nextEmployeeViDTO2);
        nextEmployeeViDTO1.setId(null);
        assertThat(nextEmployeeViDTO1).isNotEqualTo(nextEmployeeViDTO2);
    }
}
