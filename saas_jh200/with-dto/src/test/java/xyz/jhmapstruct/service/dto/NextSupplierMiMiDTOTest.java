package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierMiMiDTO.class);
        NextSupplierMiMiDTO nextSupplierMiMiDTO1 = new NextSupplierMiMiDTO();
        nextSupplierMiMiDTO1.setId(1L);
        NextSupplierMiMiDTO nextSupplierMiMiDTO2 = new NextSupplierMiMiDTO();
        assertThat(nextSupplierMiMiDTO1).isNotEqualTo(nextSupplierMiMiDTO2);
        nextSupplierMiMiDTO2.setId(nextSupplierMiMiDTO1.getId());
        assertThat(nextSupplierMiMiDTO1).isEqualTo(nextSupplierMiMiDTO2);
        nextSupplierMiMiDTO2.setId(2L);
        assertThat(nextSupplierMiMiDTO1).isNotEqualTo(nextSupplierMiMiDTO2);
        nextSupplierMiMiDTO1.setId(null);
        assertThat(nextSupplierMiMiDTO1).isNotEqualTo(nextSupplierMiMiDTO2);
    }
}
