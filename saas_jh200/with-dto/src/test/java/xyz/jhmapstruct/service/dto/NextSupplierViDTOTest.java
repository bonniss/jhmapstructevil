package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierViDTO.class);
        NextSupplierViDTO nextSupplierViDTO1 = new NextSupplierViDTO();
        nextSupplierViDTO1.setId(1L);
        NextSupplierViDTO nextSupplierViDTO2 = new NextSupplierViDTO();
        assertThat(nextSupplierViDTO1).isNotEqualTo(nextSupplierViDTO2);
        nextSupplierViDTO2.setId(nextSupplierViDTO1.getId());
        assertThat(nextSupplierViDTO1).isEqualTo(nextSupplierViDTO2);
        nextSupplierViDTO2.setId(2L);
        assertThat(nextSupplierViDTO1).isNotEqualTo(nextSupplierViDTO2);
        nextSupplierViDTO1.setId(null);
        assertThat(nextSupplierViDTO1).isNotEqualTo(nextSupplierViDTO2);
    }
}
