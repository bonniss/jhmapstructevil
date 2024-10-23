package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class SupplierAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupplierAlphaDTO.class);
        SupplierAlphaDTO supplierAlphaDTO1 = new SupplierAlphaDTO();
        supplierAlphaDTO1.setId(1L);
        SupplierAlphaDTO supplierAlphaDTO2 = new SupplierAlphaDTO();
        assertThat(supplierAlphaDTO1).isNotEqualTo(supplierAlphaDTO2);
        supplierAlphaDTO2.setId(supplierAlphaDTO1.getId());
        assertThat(supplierAlphaDTO1).isEqualTo(supplierAlphaDTO2);
        supplierAlphaDTO2.setId(2L);
        assertThat(supplierAlphaDTO1).isNotEqualTo(supplierAlphaDTO2);
        supplierAlphaDTO1.setId(null);
        assertThat(supplierAlphaDTO1).isNotEqualTo(supplierAlphaDTO2);
    }
}
