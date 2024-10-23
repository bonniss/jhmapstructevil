package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierSigmaDTO.class);
        SupplierSigmaDTO supplierSigmaDTO1 = new SupplierSigmaDTO();
        supplierSigmaDTO1.setId(1L);
        SupplierSigmaDTO supplierSigmaDTO2 = new SupplierSigmaDTO();
        assertThat(supplierSigmaDTO1).isNotEqualTo(supplierSigmaDTO2);
        supplierSigmaDTO2.setId(supplierSigmaDTO1.getId());
        assertThat(supplierSigmaDTO1).isEqualTo(supplierSigmaDTO2);
        supplierSigmaDTO2.setId(2L);
        assertThat(supplierSigmaDTO1).isNotEqualTo(supplierSigmaDTO2);
        supplierSigmaDTO1.setId(null);
        assertThat(supplierSigmaDTO1).isNotEqualTo(supplierSigmaDTO2);
    }
}
