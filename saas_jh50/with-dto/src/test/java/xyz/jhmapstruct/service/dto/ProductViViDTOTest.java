package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ProductViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductViViDTO.class);
        ProductViViDTO productViViDTO1 = new ProductViViDTO();
        productViViDTO1.setId(1L);
        ProductViViDTO productViViDTO2 = new ProductViViDTO();
        assertThat(productViViDTO1).isNotEqualTo(productViViDTO2);
        productViViDTO2.setId(productViViDTO1.getId());
        assertThat(productViViDTO1).isEqualTo(productViViDTO2);
        productViViDTO2.setId(2L);
        assertThat(productViViDTO1).isNotEqualTo(productViViDTO2);
        productViViDTO1.setId(null);
        assertThat(productViViDTO1).isNotEqualTo(productViViDTO2);
    }
}
