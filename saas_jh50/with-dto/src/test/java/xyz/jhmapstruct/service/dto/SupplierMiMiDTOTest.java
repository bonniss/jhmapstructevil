package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierMiMiDTO.class);
        SupplierMiMiDTO supplierMiMiDTO1 = new SupplierMiMiDTO();
        supplierMiMiDTO1.setId(1L);
        SupplierMiMiDTO supplierMiMiDTO2 = new SupplierMiMiDTO();
        assertThat(supplierMiMiDTO1).isNotEqualTo(supplierMiMiDTO2);
        supplierMiMiDTO2.setId(supplierMiMiDTO1.getId());
        assertThat(supplierMiMiDTO1).isEqualTo(supplierMiMiDTO2);
        supplierMiMiDTO2.setId(2L);
        assertThat(supplierMiMiDTO1).isNotEqualTo(supplierMiMiDTO2);
        supplierMiMiDTO1.setId(null);
        assertThat(supplierMiMiDTO1).isNotEqualTo(supplierMiMiDTO2);
    }
}
