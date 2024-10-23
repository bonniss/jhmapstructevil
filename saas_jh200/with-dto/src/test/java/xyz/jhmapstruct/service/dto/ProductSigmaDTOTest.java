package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductSigmaDTO.class);
        ProductSigmaDTO productSigmaDTO1 = new ProductSigmaDTO();
        productSigmaDTO1.setId(1L);
        ProductSigmaDTO productSigmaDTO2 = new ProductSigmaDTO();
        assertThat(productSigmaDTO1).isNotEqualTo(productSigmaDTO2);
        productSigmaDTO2.setId(productSigmaDTO1.getId());
        assertThat(productSigmaDTO1).isEqualTo(productSigmaDTO2);
        productSigmaDTO2.setId(2L);
        assertThat(productSigmaDTO1).isNotEqualTo(productSigmaDTO2);
        productSigmaDTO1.setId(null);
        assertThat(productSigmaDTO1).isNotEqualTo(productSigmaDTO2);
    }
}
