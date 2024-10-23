package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryGammaDTO.class);
        CategoryGammaDTO categoryGammaDTO1 = new CategoryGammaDTO();
        categoryGammaDTO1.setId(1L);
        CategoryGammaDTO categoryGammaDTO2 = new CategoryGammaDTO();
        assertThat(categoryGammaDTO1).isNotEqualTo(categoryGammaDTO2);
        categoryGammaDTO2.setId(categoryGammaDTO1.getId());
        assertThat(categoryGammaDTO1).isEqualTo(categoryGammaDTO2);
        categoryGammaDTO2.setId(2L);
        assertThat(categoryGammaDTO1).isNotEqualTo(categoryGammaDTO2);
        categoryGammaDTO1.setId(null);
        assertThat(categoryGammaDTO1).isNotEqualTo(categoryGammaDTO2);
    }
}
