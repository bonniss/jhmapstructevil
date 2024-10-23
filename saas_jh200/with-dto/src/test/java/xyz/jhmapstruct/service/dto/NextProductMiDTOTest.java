package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductMiDTO.class);
        NextProductMiDTO nextProductMiDTO1 = new NextProductMiDTO();
        nextProductMiDTO1.setId(1L);
        NextProductMiDTO nextProductMiDTO2 = new NextProductMiDTO();
        assertThat(nextProductMiDTO1).isNotEqualTo(nextProductMiDTO2);
        nextProductMiDTO2.setId(nextProductMiDTO1.getId());
        assertThat(nextProductMiDTO1).isEqualTo(nextProductMiDTO2);
        nextProductMiDTO2.setId(2L);
        assertThat(nextProductMiDTO1).isNotEqualTo(nextProductMiDTO2);
        nextProductMiDTO1.setId(null);
        assertThat(nextProductMiDTO1).isNotEqualTo(nextProductMiDTO2);
    }
}
