package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductMiMiDTO.class);
        ProductMiMiDTO productMiMiDTO1 = new ProductMiMiDTO();
        productMiMiDTO1.setId(1L);
        ProductMiMiDTO productMiMiDTO2 = new ProductMiMiDTO();
        assertThat(productMiMiDTO1).isNotEqualTo(productMiMiDTO2);
        productMiMiDTO2.setId(productMiMiDTO1.getId());
        assertThat(productMiMiDTO1).isEqualTo(productMiMiDTO2);
        productMiMiDTO2.setId(2L);
        assertThat(productMiMiDTO1).isNotEqualTo(productMiMiDTO2);
        productMiMiDTO1.setId(null);
        assertThat(productMiMiDTO1).isNotEqualTo(productMiMiDTO2);
    }
}
