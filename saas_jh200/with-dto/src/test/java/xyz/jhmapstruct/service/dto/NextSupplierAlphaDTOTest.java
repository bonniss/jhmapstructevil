package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextSupplierAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextSupplierAlphaDTO.class);
        NextSupplierAlphaDTO nextSupplierAlphaDTO1 = new NextSupplierAlphaDTO();
        nextSupplierAlphaDTO1.setId(1L);
        NextSupplierAlphaDTO nextSupplierAlphaDTO2 = new NextSupplierAlphaDTO();
        assertThat(nextSupplierAlphaDTO1).isNotEqualTo(nextSupplierAlphaDTO2);
        nextSupplierAlphaDTO2.setId(nextSupplierAlphaDTO1.getId());
        assertThat(nextSupplierAlphaDTO1).isEqualTo(nextSupplierAlphaDTO2);
        nextSupplierAlphaDTO2.setId(2L);
        assertThat(nextSupplierAlphaDTO1).isNotEqualTo(nextSupplierAlphaDTO2);
        nextSupplierAlphaDTO1.setId(null);
        assertThat(nextSupplierAlphaDTO1).isNotEqualTo(nextSupplierAlphaDTO2);
    }
}
