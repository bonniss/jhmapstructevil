package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierViViDTO.class);
        NextSupplierViViDTO nextSupplierViViDTO1 = new NextSupplierViViDTO();
        nextSupplierViViDTO1.setId(1L);
        NextSupplierViViDTO nextSupplierViViDTO2 = new NextSupplierViViDTO();
        assertThat(nextSupplierViViDTO1).isNotEqualTo(nextSupplierViViDTO2);
        nextSupplierViViDTO2.setId(nextSupplierViViDTO1.getId());
        assertThat(nextSupplierViViDTO1).isEqualTo(nextSupplierViViDTO2);
        nextSupplierViViDTO2.setId(2L);
        assertThat(nextSupplierViViDTO1).isNotEqualTo(nextSupplierViViDTO2);
        nextSupplierViViDTO1.setId(null);
        assertThat(nextSupplierViViDTO1).isNotEqualTo(nextSupplierViViDTO2);
    }
}
