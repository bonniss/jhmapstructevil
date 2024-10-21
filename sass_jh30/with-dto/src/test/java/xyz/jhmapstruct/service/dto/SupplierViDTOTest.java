package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierViDTO.class);
        SupplierViDTO supplierViDTO1 = new SupplierViDTO();
        supplierViDTO1.setId(1L);
        SupplierViDTO supplierViDTO2 = new SupplierViDTO();
        assertThat(supplierViDTO1).isNotEqualTo(supplierViDTO2);
        supplierViDTO2.setId(supplierViDTO1.getId());
        assertThat(supplierViDTO1).isEqualTo(supplierViDTO2);
        supplierViDTO2.setId(2L);
        assertThat(supplierViDTO1).isNotEqualTo(supplierViDTO2);
        supplierViDTO1.setId(null);
        assertThat(supplierViDTO1).isNotEqualTo(supplierViDTO2);
    }
}
