package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductViDTO.class);
        ProductViDTO productViDTO1 = new ProductViDTO();
        productViDTO1.setId(1L);
        ProductViDTO productViDTO2 = new ProductViDTO();
        assertThat(productViDTO1).isNotEqualTo(productViDTO2);
        productViDTO2.setId(productViDTO1.getId());
        assertThat(productViDTO1).isEqualTo(productViDTO2);
        productViDTO2.setId(2L);
        assertThat(productViDTO1).isNotEqualTo(productViDTO2);
        productViDTO1.setId(null);
        assertThat(productViDTO1).isNotEqualTo(productViDTO2);
    }
}
