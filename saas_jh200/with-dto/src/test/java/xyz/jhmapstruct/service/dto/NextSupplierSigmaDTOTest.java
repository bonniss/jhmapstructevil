package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierSigmaDTO.class);
        NextSupplierSigmaDTO nextSupplierSigmaDTO1 = new NextSupplierSigmaDTO();
        nextSupplierSigmaDTO1.setId(1L);
        NextSupplierSigmaDTO nextSupplierSigmaDTO2 = new NextSupplierSigmaDTO();
        assertThat(nextSupplierSigmaDTO1).isNotEqualTo(nextSupplierSigmaDTO2);
        nextSupplierSigmaDTO2.setId(nextSupplierSigmaDTO1.getId());
        assertThat(nextSupplierSigmaDTO1).isEqualTo(nextSupplierSigmaDTO2);
        nextSupplierSigmaDTO2.setId(2L);
        assertThat(nextSupplierSigmaDTO1).isNotEqualTo(nextSupplierSigmaDTO2);
        nextSupplierSigmaDTO1.setId(null);
        assertThat(nextSupplierSigmaDTO1).isNotEqualTo(nextSupplierSigmaDTO2);
    }
}
