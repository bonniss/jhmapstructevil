package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductViDTO.class);
        NextProductViDTO nextProductViDTO1 = new NextProductViDTO();
        nextProductViDTO1.setId(1L);
        NextProductViDTO nextProductViDTO2 = new NextProductViDTO();
        assertThat(nextProductViDTO1).isNotEqualTo(nextProductViDTO2);
        nextProductViDTO2.setId(nextProductViDTO1.getId());
        assertThat(nextProductViDTO1).isEqualTo(nextProductViDTO2);
        nextProductViDTO2.setId(2L);
        assertThat(nextProductViDTO1).isNotEqualTo(nextProductViDTO2);
        nextProductViDTO1.setId(null);
        assertThat(nextProductViDTO1).isNotEqualTo(nextProductViDTO2);
    }
}
