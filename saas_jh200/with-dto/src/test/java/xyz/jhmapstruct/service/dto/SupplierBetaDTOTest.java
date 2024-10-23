package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierBetaDTO.class);
        SupplierBetaDTO supplierBetaDTO1 = new SupplierBetaDTO();
        supplierBetaDTO1.setId(1L);
        SupplierBetaDTO supplierBetaDTO2 = new SupplierBetaDTO();
        assertThat(supplierBetaDTO1).isNotEqualTo(supplierBetaDTO2);
        supplierBetaDTO2.setId(supplierBetaDTO1.getId());
        assertThat(supplierBetaDTO1).isEqualTo(supplierBetaDTO2);
        supplierBetaDTO2.setId(2L);
        assertThat(supplierBetaDTO1).isNotEqualTo(supplierBetaDTO2);
        supplierBetaDTO1.setId(null);
        assertThat(supplierBetaDTO1).isNotEqualTo(supplierBetaDTO2);
    }
}
