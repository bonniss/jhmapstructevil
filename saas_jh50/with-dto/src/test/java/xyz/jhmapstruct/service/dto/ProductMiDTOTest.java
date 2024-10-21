package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductMiDTO.class);
        ProductMiDTO productMiDTO1 = new ProductMiDTO();
        productMiDTO1.setId(1L);
        ProductMiDTO productMiDTO2 = new ProductMiDTO();
        assertThat(productMiDTO1).isNotEqualTo(productMiDTO2);
        productMiDTO2.setId(productMiDTO1.getId());
        assertThat(productMiDTO1).isEqualTo(productMiDTO2);
        productMiDTO2.setId(2L);
        assertThat(productMiDTO1).isNotEqualTo(productMiDTO2);
        productMiDTO1.setId(null);
        assertThat(productMiDTO1).isNotEqualTo(productMiDTO2);
    }
}
