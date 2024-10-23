package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryBetaDTO.class);
        CategoryBetaDTO categoryBetaDTO1 = new CategoryBetaDTO();
        categoryBetaDTO1.setId(1L);
        CategoryBetaDTO categoryBetaDTO2 = new CategoryBetaDTO();
        assertThat(categoryBetaDTO1).isNotEqualTo(categoryBetaDTO2);
        categoryBetaDTO2.setId(categoryBetaDTO1.getId());
        assertThat(categoryBetaDTO1).isEqualTo(categoryBetaDTO2);
        categoryBetaDTO2.setId(2L);
        assertThat(categoryBetaDTO1).isNotEqualTo(categoryBetaDTO2);
        categoryBetaDTO1.setId(null);
        assertThat(categoryBetaDTO1).isNotEqualTo(categoryBetaDTO2);
    }
}
