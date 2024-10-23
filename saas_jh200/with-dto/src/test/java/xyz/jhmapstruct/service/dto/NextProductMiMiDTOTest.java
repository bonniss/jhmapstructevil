package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductMiMiDTO.class);
        NextProductMiMiDTO nextProductMiMiDTO1 = new NextProductMiMiDTO();
        nextProductMiMiDTO1.setId(1L);
        NextProductMiMiDTO nextProductMiMiDTO2 = new NextProductMiMiDTO();
        assertThat(nextProductMiMiDTO1).isNotEqualTo(nextProductMiMiDTO2);
        nextProductMiMiDTO2.setId(nextProductMiMiDTO1.getId());
        assertThat(nextProductMiMiDTO1).isEqualTo(nextProductMiMiDTO2);
        nextProductMiMiDTO2.setId(2L);
        assertThat(nextProductMiMiDTO1).isNotEqualTo(nextProductMiMiDTO2);
        nextProductMiMiDTO1.setId(null);
        assertThat(nextProductMiMiDTO1).isNotEqualTo(nextProductMiMiDTO2);
    }
}
