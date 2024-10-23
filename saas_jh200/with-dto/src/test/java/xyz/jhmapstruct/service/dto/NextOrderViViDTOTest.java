package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderViViDTO.class);
        NextOrderViViDTO nextOrderViViDTO1 = new NextOrderViViDTO();
        nextOrderViViDTO1.setId(1L);
        NextOrderViViDTO nextOrderViViDTO2 = new NextOrderViViDTO();
        assertThat(nextOrderViViDTO1).isNotEqualTo(nextOrderViViDTO2);
        nextOrderViViDTO2.setId(nextOrderViViDTO1.getId());
        assertThat(nextOrderViViDTO1).isEqualTo(nextOrderViViDTO2);
        nextOrderViViDTO2.setId(2L);
        assertThat(nextOrderViViDTO1).isNotEqualTo(nextOrderViViDTO2);
        nextOrderViViDTO1.setId(null);
        assertThat(nextOrderViViDTO1).isNotEqualTo(nextOrderViViDTO2);
    }
}
