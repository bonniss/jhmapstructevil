package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryMiMiDTO.class);
        CategoryMiMiDTO categoryMiMiDTO1 = new CategoryMiMiDTO();
        categoryMiMiDTO1.setId(1L);
        CategoryMiMiDTO categoryMiMiDTO2 = new CategoryMiMiDTO();
        assertThat(categoryMiMiDTO1).isNotEqualTo(categoryMiMiDTO2);
        categoryMiMiDTO2.setId(categoryMiMiDTO1.getId());
        assertThat(categoryMiMiDTO1).isEqualTo(categoryMiMiDTO2);
        categoryMiMiDTO2.setId(2L);
        assertThat(categoryMiMiDTO1).isNotEqualTo(categoryMiMiDTO2);
        categoryMiMiDTO1.setId(null);
        assertThat(categoryMiMiDTO1).isNotEqualTo(categoryMiMiDTO2);
    }
}
