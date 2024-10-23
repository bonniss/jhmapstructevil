package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductGammaDTO.class);
        ProductGammaDTO productGammaDTO1 = new ProductGammaDTO();
        productGammaDTO1.setId(1L);
        ProductGammaDTO productGammaDTO2 = new ProductGammaDTO();
        assertThat(productGammaDTO1).isNotEqualTo(productGammaDTO2);
        productGammaDTO2.setId(productGammaDTO1.getId());
        assertThat(productGammaDTO1).isEqualTo(productGammaDTO2);
        productGammaDTO2.setId(2L);
        assertThat(productGammaDTO1).isNotEqualTo(productGammaDTO2);
        productGammaDTO1.setId(null);
        assertThat(productGammaDTO1).isNotEqualTo(productGammaDTO2);
    }
}
