package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryAlphaDTO.class);
        CategoryAlphaDTO categoryAlphaDTO1 = new CategoryAlphaDTO();
        categoryAlphaDTO1.setId(1L);
        CategoryAlphaDTO categoryAlphaDTO2 = new CategoryAlphaDTO();
        assertThat(categoryAlphaDTO1).isNotEqualTo(categoryAlphaDTO2);
        categoryAlphaDTO2.setId(categoryAlphaDTO1.getId());
        assertThat(categoryAlphaDTO1).isEqualTo(categoryAlphaDTO2);
        categoryAlphaDTO2.setId(2L);
        assertThat(categoryAlphaDTO1).isNotEqualTo(categoryAlphaDTO2);
        categoryAlphaDTO1.setId(null);
        assertThat(categoryAlphaDTO1).isNotEqualTo(categoryAlphaDTO2);
    }
}
