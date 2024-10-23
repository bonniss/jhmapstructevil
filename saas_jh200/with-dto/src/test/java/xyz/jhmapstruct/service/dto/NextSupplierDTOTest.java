package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierDTO.class);
        NextSupplierDTO nextSupplierDTO1 = new NextSupplierDTO();
        nextSupplierDTO1.setId(1L);
        NextSupplierDTO nextSupplierDTO2 = new NextSupplierDTO();
        assertThat(nextSupplierDTO1).isNotEqualTo(nextSupplierDTO2);
        nextSupplierDTO2.setId(nextSupplierDTO1.getId());
        assertThat(nextSupplierDTO1).isEqualTo(nextSupplierDTO2);
        nextSupplierDTO2.setId(2L);
        assertThat(nextSupplierDTO1).isNotEqualTo(nextSupplierDTO2);
        nextSupplierDTO1.setId(null);
        assertThat(nextSupplierDTO1).isNotEqualTo(nextSupplierDTO2);
    }
}
