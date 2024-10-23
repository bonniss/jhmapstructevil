package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductBetaDTO.class);
        ProductBetaDTO productBetaDTO1 = new ProductBetaDTO();
        productBetaDTO1.setId(1L);
        ProductBetaDTO productBetaDTO2 = new ProductBetaDTO();
        assertThat(productBetaDTO1).isNotEqualTo(productBetaDTO2);
        productBetaDTO2.setId(productBetaDTO1.getId());
        assertThat(productBetaDTO1).isEqualTo(productBetaDTO2);
        productBetaDTO2.setId(2L);
        assertThat(productBetaDTO1).isNotEqualTo(productBetaDTO2);
        productBetaDTO1.setId(null);
        assertThat(productBetaDTO1).isNotEqualTo(productBetaDTO2);
    }
}
