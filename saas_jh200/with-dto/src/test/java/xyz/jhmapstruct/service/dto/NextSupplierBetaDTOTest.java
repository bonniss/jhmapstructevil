package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierBetaDTO.class);
        NextSupplierBetaDTO nextSupplierBetaDTO1 = new NextSupplierBetaDTO();
        nextSupplierBetaDTO1.setId(1L);
        NextSupplierBetaDTO nextSupplierBetaDTO2 = new NextSupplierBetaDTO();
        assertThat(nextSupplierBetaDTO1).isNotEqualTo(nextSupplierBetaDTO2);
        nextSupplierBetaDTO2.setId(nextSupplierBetaDTO1.getId());
        assertThat(nextSupplierBetaDTO1).isEqualTo(nextSupplierBetaDTO2);
        nextSupplierBetaDTO2.setId(2L);
        assertThat(nextSupplierBetaDTO1).isNotEqualTo(nextSupplierBetaDTO2);
        nextSupplierBetaDTO1.setId(null);
        assertThat(nextSupplierBetaDTO1).isNotEqualTo(nextSupplierBetaDTO2);
    }
}
