package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryMiDTO.class);
        CategoryMiDTO categoryMiDTO1 = new CategoryMiDTO();
        categoryMiDTO1.setId(1L);
        CategoryMiDTO categoryMiDTO2 = new CategoryMiDTO();
        assertThat(categoryMiDTO1).isNotEqualTo(categoryMiDTO2);
        categoryMiDTO2.setId(categoryMiDTO1.getId());
        assertThat(categoryMiDTO1).isEqualTo(categoryMiDTO2);
        categoryMiDTO2.setId(2L);
        assertThat(categoryMiDTO1).isNotEqualTo(categoryMiDTO2);
        categoryMiDTO1.setId(null);
        assertThat(categoryMiDTO1).isNotEqualTo(categoryMiDTO2);
    }
}
