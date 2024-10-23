package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierGammaDTO.class);
        NextSupplierGammaDTO nextSupplierGammaDTO1 = new NextSupplierGammaDTO();
        nextSupplierGammaDTO1.setId(1L);
        NextSupplierGammaDTO nextSupplierGammaDTO2 = new NextSupplierGammaDTO();
        assertThat(nextSupplierGammaDTO1).isNotEqualTo(nextSupplierGammaDTO2);
        nextSupplierGammaDTO2.setId(nextSupplierGammaDTO1.getId());
        assertThat(nextSupplierGammaDTO1).isEqualTo(nextSupplierGammaDTO2);
        nextSupplierGammaDTO2.setId(2L);
        assertThat(nextSupplierGammaDTO1).isNotEqualTo(nextSupplierGammaDTO2);
        nextSupplierGammaDTO1.setId(null);
        assertThat(nextSupplierGammaDTO1).isNotEqualTo(nextSupplierGammaDTO2);
    }
}
