package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierGammaDTO.class);
        SupplierGammaDTO supplierGammaDTO1 = new SupplierGammaDTO();
        supplierGammaDTO1.setId(1L);
        SupplierGammaDTO supplierGammaDTO2 = new SupplierGammaDTO();
        assertThat(supplierGammaDTO1).isNotEqualTo(supplierGammaDTO2);
        supplierGammaDTO2.setId(supplierGammaDTO1.getId());
        assertThat(supplierGammaDTO1).isEqualTo(supplierGammaDTO2);
        supplierGammaDTO2.setId(2L);
        assertThat(supplierGammaDTO1).isNotEqualTo(supplierGammaDTO2);
        supplierGammaDTO1.setId(null);
        assertThat(supplierGammaDTO1).isNotEqualTo(supplierGammaDTO2);
    }
}
