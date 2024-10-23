package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductThetaDTO.class);
        ProductThetaDTO productThetaDTO1 = new ProductThetaDTO();
        productThetaDTO1.setId(1L);
        ProductThetaDTO productThetaDTO2 = new ProductThetaDTO();
        assertThat(productThetaDTO1).isNotEqualTo(productThetaDTO2);
        productThetaDTO2.setId(productThetaDTO1.getId());
        assertThat(productThetaDTO1).isEqualTo(productThetaDTO2);
        productThetaDTO2.setId(2L);
        assertThat(productThetaDTO1).isNotEqualTo(productThetaDTO2);
        productThetaDTO1.setId(null);
        assertThat(productThetaDTO1).isNotEqualTo(productThetaDTO2);
    }
}
