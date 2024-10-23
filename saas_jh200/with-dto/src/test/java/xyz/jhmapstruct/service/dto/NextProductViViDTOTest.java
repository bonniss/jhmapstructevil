package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductViViDTO.class);
        NextProductViViDTO nextProductViViDTO1 = new NextProductViViDTO();
        nextProductViViDTO1.setId(1L);
        NextProductViViDTO nextProductViViDTO2 = new NextProductViViDTO();
        assertThat(nextProductViViDTO1).isNotEqualTo(nextProductViViDTO2);
        nextProductViViDTO2.setId(nextProductViViDTO1.getId());
        assertThat(nextProductViViDTO1).isEqualTo(nextProductViViDTO2);
        nextProductViViDTO2.setId(2L);
        assertThat(nextProductViViDTO1).isNotEqualTo(nextProductViViDTO2);
        nextProductViViDTO1.setId(null);
        assertThat(nextProductViViDTO1).isNotEqualTo(nextProductViViDTO2);
    }
}
