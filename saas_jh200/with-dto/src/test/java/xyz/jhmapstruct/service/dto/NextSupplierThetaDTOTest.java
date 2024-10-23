package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierThetaDTO.class);
        NextSupplierThetaDTO nextSupplierThetaDTO1 = new NextSupplierThetaDTO();
        nextSupplierThetaDTO1.setId(1L);
        NextSupplierThetaDTO nextSupplierThetaDTO2 = new NextSupplierThetaDTO();
        assertThat(nextSupplierThetaDTO1).isNotEqualTo(nextSupplierThetaDTO2);
        nextSupplierThetaDTO2.setId(nextSupplierThetaDTO1.getId());
        assertThat(nextSupplierThetaDTO1).isEqualTo(nextSupplierThetaDTO2);
        nextSupplierThetaDTO2.setId(2L);
        assertThat(nextSupplierThetaDTO1).isNotEqualTo(nextSupplierThetaDTO2);
        nextSupplierThetaDTO1.setId(null);
        assertThat(nextSupplierThetaDTO1).isNotEqualTo(nextSupplierThetaDTO2);
    }
}
