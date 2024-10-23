package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextEmployeeViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextEmployeeViViDTO.class);
        NextEmployeeViViDTO nextEmployeeViViDTO1 = new NextEmployeeViViDTO();
        nextEmployeeViViDTO1.setId(1L);
        NextEmployeeViViDTO nextEmployeeViViDTO2 = new NextEmployeeViViDTO();
        assertThat(nextEmployeeViViDTO1).isNotEqualTo(nextEmployeeViViDTO2);
        nextEmployeeViViDTO2.setId(nextEmployeeViViDTO1.getId());
        assertThat(nextEmployeeViViDTO1).isEqualTo(nextEmployeeViViDTO2);
        nextEmployeeViViDTO2.setId(2L);
        assertThat(nextEmployeeViViDTO1).isNotEqualTo(nextEmployeeViViDTO2);
        nextEmployeeViViDTO1.setId(null);
        assertThat(nextEmployeeViViDTO1).isNotEqualTo(nextEmployeeViViDTO2);
    }
}
