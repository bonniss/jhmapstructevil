package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierThetaDTO.class);
        SupplierThetaDTO supplierThetaDTO1 = new SupplierThetaDTO();
        supplierThetaDTO1.setId(1L);
        SupplierThetaDTO supplierThetaDTO2 = new SupplierThetaDTO();
        assertThat(supplierThetaDTO1).isNotEqualTo(supplierThetaDTO2);
        supplierThetaDTO2.setId(supplierThetaDTO1.getId());
        assertThat(supplierThetaDTO1).isEqualTo(supplierThetaDTO2);
        supplierThetaDTO2.setId(2L);
        assertThat(supplierThetaDTO1).isNotEqualTo(supplierThetaDTO2);
        supplierThetaDTO1.setId(null);
        assertThat(supplierThetaDTO1).isNotEqualTo(supplierThetaDTO2);
    }
}
