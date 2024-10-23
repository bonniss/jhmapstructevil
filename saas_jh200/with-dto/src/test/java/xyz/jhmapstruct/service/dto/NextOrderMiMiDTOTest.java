package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderMiMiDTO.class);
        NextOrderMiMiDTO nextOrderMiMiDTO1 = new NextOrderMiMiDTO();
        nextOrderMiMiDTO1.setId(1L);
        NextOrderMiMiDTO nextOrderMiMiDTO2 = new NextOrderMiMiDTO();
        assertThat(nextOrderMiMiDTO1).isNotEqualTo(nextOrderMiMiDTO2);
        nextOrderMiMiDTO2.setId(nextOrderMiMiDTO1.getId());
        assertThat(nextOrderMiMiDTO1).isEqualTo(nextOrderMiMiDTO2);
        nextOrderMiMiDTO2.setId(2L);
        assertThat(nextOrderMiMiDTO1).isNotEqualTo(nextOrderMiMiDTO2);
        nextOrderMiMiDTO1.setId(null);
        assertThat(nextOrderMiMiDTO1).isNotEqualTo(nextOrderMiMiDTO2);
    }
}
