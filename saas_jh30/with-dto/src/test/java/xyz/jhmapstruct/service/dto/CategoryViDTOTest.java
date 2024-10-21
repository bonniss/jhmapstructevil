package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryViDTO.class);
        CategoryViDTO categoryViDTO1 = new CategoryViDTO();
        categoryViDTO1.setId(1L);
        CategoryViDTO categoryViDTO2 = new CategoryViDTO();
        assertThat(categoryViDTO1).isNotEqualTo(categoryViDTO2);
        categoryViDTO2.setId(categoryViDTO1.getId());
        assertThat(categoryViDTO1).isEqualTo(categoryViDTO2);
        categoryViDTO2.setId(2L);
        assertThat(categoryViDTO1).isNotEqualTo(categoryViDTO2);
        categoryViDTO1.setId(null);
        assertThat(categoryViDTO1).isNotEqualTo(categoryViDTO2);
    }
}
