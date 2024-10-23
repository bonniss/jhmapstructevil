package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderViDTO.class);
        NextOrderViDTO nextOrderViDTO1 = new NextOrderViDTO();
        nextOrderViDTO1.setId(1L);
        NextOrderViDTO nextOrderViDTO2 = new NextOrderViDTO();
        assertThat(nextOrderViDTO1).isNotEqualTo(nextOrderViDTO2);
        nextOrderViDTO2.setId(nextOrderViDTO1.getId());
        assertThat(nextOrderViDTO1).isEqualTo(nextOrderViDTO2);
        nextOrderViDTO2.setId(2L);
        assertThat(nextOrderViDTO1).isNotEqualTo(nextOrderViDTO2);
        nextOrderViDTO1.setId(null);
        assertThat(nextOrderViDTO1).isNotEqualTo(nextOrderViDTO2);
    }
}
