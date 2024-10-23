package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierMiDTO.class);
        NextSupplierMiDTO nextSupplierMiDTO1 = new NextSupplierMiDTO();
        nextSupplierMiDTO1.setId(1L);
        NextSupplierMiDTO nextSupplierMiDTO2 = new NextSupplierMiDTO();
        assertThat(nextSupplierMiDTO1).isNotEqualTo(nextSupplierMiDTO2);
        nextSupplierMiDTO2.setId(nextSupplierMiDTO1.getId());
        assertThat(nextSupplierMiDTO1).isEqualTo(nextSupplierMiDTO2);
        nextSupplierMiDTO2.setId(2L);
        assertThat(nextSupplierMiDTO1).isNotEqualTo(nextSupplierMiDTO2);
        nextSupplierMiDTO1.setId(null);
        assertThat(nextSupplierMiDTO1).isNotEqualTo(nextSupplierMiDTO2);
    }
}
