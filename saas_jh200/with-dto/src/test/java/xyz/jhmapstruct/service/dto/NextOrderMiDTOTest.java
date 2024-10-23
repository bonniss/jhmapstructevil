package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderMiDTO.class);
        NextOrderMiDTO nextOrderMiDTO1 = new NextOrderMiDTO();
        nextOrderMiDTO1.setId(1L);
        NextOrderMiDTO nextOrderMiDTO2 = new NextOrderMiDTO();
        assertThat(nextOrderMiDTO1).isNotEqualTo(nextOrderMiDTO2);
        nextOrderMiDTO2.setId(nextOrderMiDTO1.getId());
        assertThat(nextOrderMiDTO1).isEqualTo(nextOrderMiDTO2);
        nextOrderMiDTO2.setId(2L);
        assertThat(nextOrderMiDTO1).isNotEqualTo(nextOrderMiDTO2);
        nextOrderMiDTO1.setId(null);
        assertThat(nextOrderMiDTO1).isNotEqualTo(nextOrderMiDTO2);
    }
}
