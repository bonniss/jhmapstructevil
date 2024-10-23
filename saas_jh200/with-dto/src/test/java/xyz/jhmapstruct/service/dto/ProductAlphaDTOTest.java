package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAlphaDTO.class);
        ProductAlphaDTO productAlphaDTO1 = new ProductAlphaDTO();
        productAlphaDTO1.setId(1L);
        ProductAlphaDTO productAlphaDTO2 = new ProductAlphaDTO();
        assertThat(productAlphaDTO1).isNotEqualTo(productAlphaDTO2);
        productAlphaDTO2.setId(productAlphaDTO1.getId());
        assertThat(productAlphaDTO1).isEqualTo(productAlphaDTO2);
        productAlphaDTO2.setId(2L);
        assertThat(productAlphaDTO1).isNotEqualTo(productAlphaDTO2);
        productAlphaDTO1.setId(null);
        assertThat(productAlphaDTO1).isNotEqualTo(productAlphaDTO2);
    }
}
