package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductDTO.class);
        NextProductDTO nextProductDTO1 = new NextProductDTO();
        nextProductDTO1.setId(1L);
        NextProductDTO nextProductDTO2 = new NextProductDTO();
        assertThat(nextProductDTO1).isNotEqualTo(nextProductDTO2);
        nextProductDTO2.setId(nextProductDTO1.getId());
        assertThat(nextProductDTO1).isEqualTo(nextProductDTO2);
        nextProductDTO2.setId(2L);
        assertThat(nextProductDTO1).isNotEqualTo(nextProductDTO2);
        nextProductDTO1.setId(null);
        assertThat(nextProductDTO1).isNotEqualTo(nextProductDTO2);
    }
}
