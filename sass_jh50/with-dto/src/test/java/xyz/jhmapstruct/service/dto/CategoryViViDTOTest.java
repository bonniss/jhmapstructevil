package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryViViDTO.class);
        CategoryViViDTO categoryViViDTO1 = new CategoryViViDTO();
        categoryViViDTO1.setId(1L);
        CategoryViViDTO categoryViViDTO2 = new CategoryViViDTO();
        assertThat(categoryViViDTO1).isNotEqualTo(categoryViViDTO2);
        categoryViViDTO2.setId(categoryViViDTO1.getId());
        assertThat(categoryViViDTO1).isEqualTo(categoryViViDTO2);
        categoryViViDTO2.setId(2L);
        assertThat(categoryViViDTO1).isNotEqualTo(categoryViViDTO2);
        categoryViViDTO1.setId(null);
        assertThat(categoryViViDTO1).isNotEqualTo(categoryViViDTO2);
    }
}
