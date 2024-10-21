package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierDTO.class);
        SupplierDTO supplierDTO1 = new SupplierDTO();
        supplierDTO1.setId(1L);
        SupplierDTO supplierDTO2 = new SupplierDTO();
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
        supplierDTO2.setId(supplierDTO1.getId());
        assertThat(supplierDTO1).isEqualTo(supplierDTO2);
        supplierDTO2.setId(2L);
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
        supplierDTO1.setId(null);
        assertThat(supplierDTO1).isNotEqualTo(supplierDTO2);
    }
}
