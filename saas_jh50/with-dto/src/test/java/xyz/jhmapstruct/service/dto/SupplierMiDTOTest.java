package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierMiDTO.class);
        SupplierMiDTO supplierMiDTO1 = new SupplierMiDTO();
        supplierMiDTO1.setId(1L);
        SupplierMiDTO supplierMiDTO2 = new SupplierMiDTO();
        assertThat(supplierMiDTO1).isNotEqualTo(supplierMiDTO2);
        supplierMiDTO2.setId(supplierMiDTO1.getId());
        assertThat(supplierMiDTO1).isEqualTo(supplierMiDTO2);
        supplierMiDTO2.setId(2L);
        assertThat(supplierMiDTO1).isNotEqualTo(supplierMiDTO2);
        supplierMiDTO1.setId(null);
        assertThat(supplierMiDTO1).isNotEqualTo(supplierMiDTO2);
    }
}
