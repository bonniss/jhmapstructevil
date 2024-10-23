package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderDTO.class);
        NextOrderDTO nextOrderDTO1 = new NextOrderDTO();
        nextOrderDTO1.setId(1L);
        NextOrderDTO nextOrderDTO2 = new NextOrderDTO();
        assertThat(nextOrderDTO1).isNotEqualTo(nextOrderDTO2);
        nextOrderDTO2.setId(nextOrderDTO1.getId());
        assertThat(nextOrderDTO1).isEqualTo(nextOrderDTO2);
        nextOrderDTO2.setId(2L);
        assertThat(nextOrderDTO1).isNotEqualTo(nextOrderDTO2);
        nextOrderDTO1.setId(null);
        assertThat(nextOrderDTO1).isNotEqualTo(nextOrderDTO2);
    }
}
