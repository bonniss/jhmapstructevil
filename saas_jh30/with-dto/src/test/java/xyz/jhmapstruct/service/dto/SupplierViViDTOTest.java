package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierViViDTO.class);
        SupplierViViDTO supplierViViDTO1 = new SupplierViViDTO();
        supplierViViDTO1.setId(1L);
        SupplierViViDTO supplierViViDTO2 = new SupplierViViDTO();
        assertThat(supplierViViDTO1).isNotEqualTo(supplierViViDTO2);
        supplierViViDTO2.setId(supplierViViDTO1.getId());
        assertThat(supplierViViDTO1).isEqualTo(supplierViViDTO2);
        supplierViViDTO2.setId(2L);
        assertThat(supplierViViDTO1).isNotEqualTo(supplierViViDTO2);
        supplierViViDTO1.setId(null);
        assertThat(supplierViViDTO1).isNotEqualTo(supplierViViDTO2);
    }
}
